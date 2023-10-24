package org.folio.linked.data.mapper.resource.monograph.inner.work.sub.creator;

import static org.folio.ld.dictionary.PredicateDictionary.CREATOR;
import static org.folio.ld.dictionary.ResourceTypeDictionary.FAMILY;

import org.folio.linked.data.domain.dto.Agent;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.springframework.stereotype.Component;

@Component("CreatorFamilyMapperUnit")
@MapperUnit(type = FAMILY, dtoClass = Agent.class, predicate = CREATOR)
public class FamilyMapperUnit extends CreatorMapperUnit {
  public FamilyMapperUnit(CoreMapper coreMapper) {
    super(coreMapper, FAMILY_CONVERTER);
  }
}
