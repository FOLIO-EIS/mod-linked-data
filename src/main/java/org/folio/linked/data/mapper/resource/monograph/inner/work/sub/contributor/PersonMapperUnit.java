package org.folio.linked.data.mapper.resource.monograph.inner.work.sub.contributor;

import static org.folio.ld.dictionary.PredicateDictionary.CONTRIBUTOR;
import static org.folio.ld.dictionary.ResourceTypeDictionary.PERSON;

import org.folio.linked.data.domain.dto.Agent;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.springframework.stereotype.Component;

@Component("ContributorPersonMapperUnit")
@MapperUnit(type = PERSON, dtoClass = Agent.class, predicate = CONTRIBUTOR)
public class PersonMapperUnit extends ContributorMapperUnit {
  public PersonMapperUnit(CoreMapper coreMapper) {
    super(coreMapper, PERSON_CONVERTER);
  }
}