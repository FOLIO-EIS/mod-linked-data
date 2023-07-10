package org.folio.linked.data.mapper.resource.monograph.inner.work.sub;

import static org.folio.linked.data.util.BibframeConstants.OTHER_EDITION_PRED;

import lombok.RequiredArgsConstructor;
import org.folio.linked.data.domain.dto.Work;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(predicate = OTHER_EDITION_PRED)
public class OtherEditionMapperUnit implements WorkSubResourceMapperUnit {

  private final CoreMapper coreMapper;

  @Override
  public Work toDto(Resource source, Work destination) {
    var property = coreMapper.toProperty(source);
    destination.addOtherEditionItem(property);
    return destination;
  }
}