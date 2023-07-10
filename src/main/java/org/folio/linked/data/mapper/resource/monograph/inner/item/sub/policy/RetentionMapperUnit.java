package org.folio.linked.data.mapper.resource.monograph.inner.item.sub.policy;

import static org.folio.linked.data.util.BibframeConstants.ITEM_RETENTION;

import lombok.RequiredArgsConstructor;
import org.folio.linked.data.domain.dto.Item;
import org.folio.linked.data.domain.dto.RetentionPolicy;
import org.folio.linked.data.domain.dto.RetentionPolicyField;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.mapper.resource.monograph.inner.item.sub.ItemSubResourceMapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(type = ITEM_RETENTION)
public class RetentionMapperUnit implements ItemSubResourceMapperUnit {

  private final CoreMapper coreMapper;

  @Override
  public Item toDto(Resource source, Item destination) {
    var policy = coreMapper.readResourceDoc(source, RetentionPolicy.class);
    destination.addUsageAndAccessPolicyItem(new RetentionPolicyField().retentionPolicy(policy));
    return destination;
  }
}