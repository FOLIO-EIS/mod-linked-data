package org.folio.linked.data.mapper.resource.monograph.common;

import static org.folio.ld.dictionary.PropertyDictionary.LABEL;
import static org.folio.ld.dictionary.PropertyDictionary.LINK;
import static org.folio.ld.dictionary.ResourceTypeDictionary.STATUS;
import static org.folio.linked.data.util.BibframeUtils.getFirstValue;
import static org.folio.linked.data.util.BibframeUtils.putProperty;
import static org.folio.linked.data.util.Constants.IS_NOT_SUPPORTED_FOR_PREDICATE;
import static org.folio.linked.data.util.Constants.RESOURCE_TYPE;
import static org.folio.linked.data.util.Constants.RIGHT_SQUARE_BRACKET;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.folio.ld.dictionary.PredicateDictionary;
import org.folio.linked.data.domain.dto.Isbn;
import org.folio.linked.data.domain.dto.Lccn;
import org.folio.linked.data.domain.dto.Status;
import org.folio.linked.data.exception.NotSupportedException;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.mapper.resource.common.sub.SubResourceMapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(type = STATUS, predicate = PredicateDictionary.STATUS, dtoClass = Status.class)
public class StatusMapperUnit implements SubResourceMapperUnit {

  private static final Set<Class<?>> SUPPORTED_PARENTS = Set.of(Lccn.class, Isbn.class);
  private final CoreMapper coreMapper;

  @Override
  public <T> T toDto(Resource source, T destination) {
    var status = coreMapper.readResourceDoc(source, Status.class);
    status.setId(String.valueOf(source.getResourceHash()));
    if (destination instanceof Lccn lccn) {
      lccn.addStatusItem(status);
    } else if (destination instanceof Isbn isbn) {
      isbn.addStatusItem(status);
    } else {
      throw new NotSupportedException(RESOURCE_TYPE + destination.getClass().getSimpleName()
        + IS_NOT_SUPPORTED_FOR_PREDICATE + PredicateDictionary.STATUS.getUri() + RIGHT_SQUARE_BRACKET);
    }
    return destination;
  }

  @Override
  public Set<Class<?>> getParentDto() {
    return SUPPORTED_PARENTS;
  }

  @Override
  public Resource toEntity(Object dto) {
    var status = (Status) dto;
    var resource = new Resource();
    resource.setLabel(getFirstValue(status::getValue));
    resource.addType(STATUS);
    resource.setDoc(getDoc(status));
    resource.setResourceHash(coreMapper.hash(resource));
    return resource;
  }

  private JsonNode getDoc(Status dto) {
    var map = new HashMap<String, List<String>>();
    putProperty(map, LINK, dto.getLink());
    putProperty(map, LABEL, dto.getValue());
    return map.isEmpty() ? null : coreMapper.toJson(map);
  }

}
