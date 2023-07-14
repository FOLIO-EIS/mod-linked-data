package org.folio.linked.data.mapper.resource.monograph.inner.instance.sub.identified;

import static org.folio.linked.data.util.BibframeConstants.IDENTIFIED_BY_PRED;
import static org.folio.linked.data.util.BibframeConstants.IDENTIFIERS_EAN;
import static org.folio.linked.data.util.BibframeConstants.IDENTIFIERS_EAN_URL;
import static org.folio.linked.data.util.BibframeConstants.QUALIFIER_URL;
import static org.folio.linked.data.util.BibframeConstants.VALUE_PRED;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.folio.linked.data.domain.dto.Ean;
import org.folio.linked.data.domain.dto.EanField;
import org.folio.linked.data.domain.dto.Instance;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.mapper.resource.monograph.inner.instance.sub.InstanceSubResourceMapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.model.entity.ResourceType;
import org.folio.linked.data.service.dictionary.DictionaryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(type = IDENTIFIERS_EAN, predicate = IDENTIFIED_BY_PRED, dtoClass = EanField.class)
public class EanMapperUnit implements InstanceSubResourceMapperUnit {

  private final DictionaryService<ResourceType> resourceTypeService;
  private final CoreMapper coreMapper;

  @Override
  public Instance toDto(Resource source, Instance destination) {
    var ean = coreMapper.readResourceDoc(source, Ean.class);
    destination.addIdentifiedByItem(new EanField().ean(ean));
    return destination;
  }

  @Override
  public Resource toEntity(Object dto, String predicate) {
    var ean = ((EanField) dto).getEan();
    var resource = new Resource();
    resource.setLabel(IDENTIFIERS_EAN_URL);
    resource.setType(resourceTypeService.get(IDENTIFIERS_EAN));
    resource.setDoc(getDoc(ean));
    resource.setResourceHash(coreMapper.hash(resource));
    return resource;
  }

  private JsonNode getDoc(Ean dto) {
    var map = new HashMap<String, List<String>>();
    map.put(VALUE_PRED, dto.getValue());
    map.put(QUALIFIER_URL, dto.getQualifier());
    return coreMapper.toJson(map);
  }

}
