package org.folio.linked.data.service.resource;

import static org.folio.linked.data.util.Constants.IS_NOT_FOUND;
import static org.folio.linked.data.util.Constants.RESOURCE_WITH_GIVEN_ID;
import static org.folio.linked.data.util.Constants.RESOURCE_WITH_GIVEN_INVENTORY_ID;

import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.folio.linked.data.domain.dto.ResourceIdDto;
import org.folio.linked.data.domain.dto.ResourceRequestDto;
import org.folio.linked.data.domain.dto.ResourceResponseDto;
import org.folio.linked.data.exception.NotFoundException;
import org.folio.linked.data.mapper.dto.ResourceDtoMapper;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.model.entity.event.ResourceCreatedEvent;
import org.folio.linked.data.model.entity.event.ResourceDeletedEvent;
import org.folio.linked.data.model.entity.event.ResourceReplacedEvent;
import org.folio.linked.data.model.entity.event.ResourceUpdatedEvent;
import org.folio.linked.data.repo.FolioMetadataRepository;
import org.folio.linked.data.repo.ResourceRepository;
import org.folio.linked.data.service.resource.meta.MetadataService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

  private final FolioMetadataRepository folioMetadataRepo;
  private final ResourceRepository resourceRepo;
  private final ResourceDtoMapper resourceDtoMapper;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final MetadataService metadataService;
  private final ResourceGraphService resourceGraphService;

  @Override
  public ResourceResponseDto createResource(ResourceRequestDto resourceDto) {
    var mapped = resourceDtoMapper.toEntity(resourceDto);
    log.info("createResource\n[{}]\nfrom DTO [{}]", mapped, resourceDto);
    metadataService.ensure(mapped);
    var persisted = resourceGraphService.saveMergingGraph(mapped);
    applicationEventPublisher.publishEvent(new ResourceCreatedEvent(persisted));
    return resourceDtoMapper.toDto(persisted);
  }

  @Override
  @Transactional(readOnly = true)
  public ResourceResponseDto getResourceById(Long id) {
    var resource = getResource(id);
    return resourceDtoMapper.toDto(resource);
  }

  @Override
  public ResourceIdDto getResourceIdByInventoryId(String inventoryId) {
    return folioMetadataRepo.findIdByInventoryId(inventoryId)
      .map(idOnly -> new ResourceIdDto().id(String.valueOf(idOnly.getId())))
      .orElseThrow(() -> new NotFoundException(RESOURCE_WITH_GIVEN_INVENTORY_ID + inventoryId + IS_NOT_FOUND));
  }

  @Override
  public ResourceResponseDto updateResource(Long id, ResourceRequestDto resourceDto) {
    log.info("updateResource [{}] from DTO [{}]", id, resourceDto);
    var existed = getResource(id);
    var oldResource = new Resource(existed);
    resourceGraphService.breakEdgesAndDelete(existed);
    var newResource = saveNewResource(resourceDto, oldResource);
    if (Objects.equals(oldResource.getId(), newResource.getId())) {
      applicationEventPublisher.publishEvent(new ResourceUpdatedEvent(newResource));
    } else {
      applicationEventPublisher.publishEvent(new ResourceReplacedEvent(oldResource, newResource));
    }
    return resourceDtoMapper.toDto(newResource);
  }


  @Override
  public void deleteResource(Long id) {
    log.info("deleteResource [{}]", id);
    resourceRepo.findById(id).ifPresent(resource -> {
      resourceGraphService.breakEdgesAndDelete(resource);
      applicationEventPublisher.publishEvent(new ResourceDeletedEvent(resource));
    });
  }

  @Async
  @Override
  public void updateIndexDateBatch(Set<Long> ids) {
    resourceRepo.updateIndexDateBatch(ids);
  }

  private Resource getResource(Long id) {
    return resourceRepo.findById(id)
      .orElseThrow(() -> new NotFoundException(RESOURCE_WITH_GIVEN_ID + id + IS_NOT_FOUND));
  }

  private Resource saveNewResource(ResourceRequestDto resourceDto, Resource old) {
    var mapped = resourceDtoMapper.toEntity(resourceDto);
    metadataService.ensure(mapped, old.getFolioMetadata());
    return resourceGraphService.saveMergingGraph(mapped);
  }

}
