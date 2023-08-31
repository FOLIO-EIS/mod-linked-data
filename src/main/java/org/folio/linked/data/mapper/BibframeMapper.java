package org.folio.linked.data.mapper;

import static org.apache.commons.lang3.StringUtils.firstNonEmpty;
import static org.folio.linked.data.util.Bibframe2Constants.DATE_URL;
import static org.folio.linked.data.util.Bibframe2Constants.EDITION_STATEMENT_URL;
import static org.folio.linked.data.util.Bibframe2Constants.IDENTIFIED_BY_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.INSTANCE_URL;
import static org.folio.linked.data.util.Bibframe2Constants.PROVISION_ACTIVITY_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.PUBLICATION;
import static org.folio.linked.data.util.Bibframe2Constants.SIMPLE_AGENT_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.SIMPLE_DATE_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.SIMPLE_PLACE_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.VALUE_PRED;
import static org.folio.linked.data.util.BibframeConstants.INSTANCE;
import static org.folio.linked.data.util.BibframeConstants.ITEM;
import static org.folio.linked.data.util.BibframeConstants.WORK;
import static org.folio.search.domain.dto.BibframeIdentifiersInner.TypeEnum;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.folio.linked.data.domain.dto.Bibframe2Request;
import org.folio.linked.data.domain.dto.Bibframe2Response;
import org.folio.linked.data.domain.dto.BibframeRequest;
import org.folio.linked.data.domain.dto.BibframeResponse;
import org.folio.linked.data.domain.dto.BibframeShort;
import org.folio.linked.data.domain.dto.BibframeShortInfoPage;
import org.folio.linked.data.exception.NotSupportedException;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.ProfiledMapper;
import org.folio.linked.data.mapper.resource.common.inner.InnerResourceMapper;
import org.folio.linked.data.model.ResourceShortInfo;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.model.entity.ResourceEdge;
import org.folio.linked.data.model.entity.ResourceType;
import org.folio.linked.data.service.dictionary.DictionaryService;
import org.folio.search.domain.dto.BibframeContributorsInner;
import org.folio.search.domain.dto.BibframeIdentifiersInner;
import org.folio.search.domain.dto.BibframeIndex;
import org.folio.search.domain.dto.BibframePublicationsInner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

@Mapper(componentModel = SPRING)
public abstract class BibframeMapper {

  @Autowired
  private ProfiledMapper profiledMapper;
  @Autowired
  private DictionaryService<ResourceType> resourceTypeService;
  @Autowired
  private InnerResourceMapper innerMapper;
  @Autowired
  private CoreMapper coreMapper;

  @Mapping(target = "id", source = "resourceHash")
  @Mapping(target = "profile", expression = "java(resourceShortInfo.getType().getSimpleLabel())")
  @Mapping(target = "type", expression = "java(resourceShortInfo.getType().getSimpleLabel())")
  public abstract BibframeShort map(ResourceShortInfo resourceShortInfo);

  public abstract BibframeShortInfoPage map(Page<BibframeShort> page);

  @SneakyThrows
  public Resource toEntity(BibframeRequest dto) {
    var bibframe = new Resource();
    bibframe.setType(resourceTypeService.get(dto.getType()));
    coreMapper.mapResourceEdges(dto.getWork(), bibframe, WORK, WORK, innerMapper::toEntity);
    coreMapper.mapResourceEdges(dto.getInstance(), bibframe, INSTANCE, INSTANCE, innerMapper::toEntity);
    coreMapper.mapResourceEdges(dto.getItem(), bibframe, ITEM, ITEM, innerMapper::toEntity);
    bibframe.setResourceHash(coreMapper.hash(bibframe));
    bibframe.setLabel(getInstanceLabel(bibframe));
    return bibframe;
  }

  public BibframeResponse toDto(Resource resource) {
    var response = new BibframeResponse()
      .id(resource.getResourceHash())
      .type(resource.getType().getTypeUri());
    resource.getOutgoingEdges().stream()
      .map(ResourceEdge::getTarget)
      .forEach(r -> innerMapper.toDto(r, response));
    return response;
  }

  public Resource toEntity2(Bibframe2Request dto) {
    var resource = profiledMapper.toEntity(dto);
    setEdgesId(resource);
    return resource;
  }

  public Bibframe2Response toDto2(Resource resource) {
    return profiledMapper.toDto(resource);
  }

  public BibframeIndex mapToIndex2(@NonNull Resource resource) {
    var instance = extractInstance(resource);
    var bibframeIndex = new BibframeIndex(resource.getResourceHash().toString());
    bibframeIndex.setTitle(instance.getLabel());
    bibframeIndex.setIdentifiers(extractIdentifiers(instance));
    bibframeIndex.setContributors(extractContributors(instance));
    bibframeIndex.setPublications(extractPublications(instance));
    bibframeIndex.setEditionStatement(getValue(instance.getDoc(), EDITION_STATEMENT_URL));
    return bibframeIndex;
  }

  private String getInstanceLabel(Resource bibframe) {
    return bibframe.getOutgoingEdges().stream()
      .filter(re -> INSTANCE.equals(re.getPredicate().getLabel()))
      .map(ResourceEdge::getTarget)
      .map(Resource::getLabel)
      .findFirst().orElse("");
  }

  private Resource extractInstance(Resource resource) {
    return resource.getOutgoingEdges().stream()
      .filter(re -> INSTANCE_URL.equals(re.getPredicate().getLabel()))
      .map(ResourceEdge::getTarget)
      .findFirst()
      .orElseThrow(() -> new NotSupportedException("Only Monograph.Instance bibframe is supported for now, and there "
        + "is no Instance found"));
  }

  private List<BibframeIdentifiersInner> extractIdentifiers(Resource resource) {
    return resource.getOutgoingEdges().stream()
      .filter(re -> IDENTIFIED_BY_PRED.equals(re.getPredicate().getLabel()))
      .map(ResourceEdge::getTarget)
      .map(ir -> new BibframeIdentifiersInner()
        .value(getValue(ir.getDoc(), VALUE_PRED))
        .type(TypeEnum.fromValue(ir.getType().getSimpleLabel().replace("lc:RT:bf2:Identifiers:", ""))))
      .toList();
  }

  private List<BibframeContributorsInner> extractContributors(Resource resource) {
    return new ArrayList<>(); // Lookup fields are not supported at the moment
  }

  private List<BibframePublicationsInner> extractPublications(Resource resource) {
    return resource.getOutgoingEdges().stream()
      .filter(re -> PROVISION_ACTIVITY_PRED.equals(re.getPredicate().getLabel()))
      .map(ResourceEdge::getTarget)
      .filter(r -> PUBLICATION.equals(r.getType().getSimpleLabel()))
      .map(Resource::getDoc)
      .map(doc -> new BibframePublicationsInner()
        .dateOfPublication(firstNonEmpty(getValue(doc, SIMPLE_DATE_PRED), getValue(doc, DATE_URL)))
        .publisher(firstNonEmpty(getValue(doc, SIMPLE_AGENT_PRED), getValue(doc, SIMPLE_PLACE_PRED)))
      )
      .toList();
  }

  private String getValue(JsonNode doc, String value) {
    return doc.has(value)
      ? doc.get(value).get(0).textValue()
      : null;
  }

  private void setEdgesId(Resource resource) {
    resource.getOutgoingEdges().forEach(edge -> {
      edge.getId().setSourceHash(edge.getSource().getResourceHash());
      edge.getId().setTargetHash(edge.getTarget().getResourceHash());
      edge.getId().setPredicateHash(edge.getPredicate().getPredicateHash());
      setEdgesId(edge.getTarget());
    });
  }

}
