package org.folio.linked.data.mapper.dto.monograph.instance.sub;

import java.util.Set;
import org.folio.linked.data.domain.dto.Instance;
import org.folio.linked.data.domain.dto.InstanceReference;
import org.folio.linked.data.mapper.dto.common.SingleResourceMapperUnit;

public interface InstanceSubResourceMapperUnit extends SingleResourceMapperUnit {

  @Override
  default Set<Class<?>> supportedParents() {
    return Set.of(Instance.class, InstanceReference.class);
  }
}