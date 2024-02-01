package org.folio.linked.data.mapper.resource.monograph.instance;

import static java.util.Objects.nonNull;
import static org.folio.ld.dictionary.PredicateDictionary.INSTANTIATES;
import static org.folio.ld.dictionary.ResourceTypeDictionary.INSTANCE;

import java.util.function.Consumer;
import org.folio.linked.data.domain.dto.InstanceReference;
import org.folio.linked.data.domain.dto.Work;
import org.folio.linked.data.exception.NotFoundException;
import org.folio.linked.data.exception.ValidationException;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.mapper.resource.monograph.work.sub.WorkSubResourceMapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.repo.ResourceRepository;
import org.springframework.stereotype.Component;

@Component
@MapperUnit(type = INSTANCE, predicate = INSTANTIATES, dtoClass = InstanceReference.class)
public class InstanceReferenceMapperUnit implements WorkSubResourceMapperUnit {

  private final CoreMapper coreMapper;
  private final ResourceRepository resourceRepository;

  public InstanceReferenceMapperUnit(CoreMapper coreMapper, ResourceRepository resourceRepository) {
    this.coreMapper = coreMapper;
    this.resourceRepository = resourceRepository;
  }

  @Override
  public <T> T toDto(Resource source, T parentDto, Resource parentResource) {
    Consumer<InstanceReference> instanceConsumer = instance -> handleMappedInstance(source, parentDto, instance);
    coreMapper.mapToDtoWithEdges(source, instanceConsumer, InstanceReference.class);
    return parentDto;
  }

  @Override
  public Resource toEntity(Object dto, Resource parentEntity) {
    var instance = (InstanceReference) dto;
    if (nonNull(instance.getId())) {
      return resourceRepository.findById(Long.parseLong(instance.getId()))
        .orElseThrow(() -> new NotFoundException("Instance with id [" + instance.getId() + " is not found"));
    } else {
      throw new ValidationException("Instance id", "null");
    }
  }

  private <T> void handleMappedInstance(Resource source, T destination, InstanceReference instance) {
    instance.setId(String.valueOf(source.getResourceHash()));
    if (destination instanceof Work work) {
      work.addInstanceReferenceItem(instance);
    }
  }

}
