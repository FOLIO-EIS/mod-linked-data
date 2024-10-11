package org.folio.linked.data.mapper.kafka.inventory;

import static org.folio.linked.data.util.Constants.SEARCH_INSTANCE_RESOURCE_NAME;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;
import org.folio.linked.data.domain.dto.InventoryInstanceEvent;
import org.folio.linked.data.domain.dto.LinkedDataInstance;
import org.folio.linked.data.domain.dto.LinkedDataInstanceOnlySuppress;
import org.folio.linked.data.domain.dto.ResourceIndexEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, imports = UUID.class)
public abstract class InventoryInstanceMapper {

  @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
  @Mapping(target = "resourceName", constant = SEARCH_INSTANCE_RESOURCE_NAME)
  @Mapping(target = "_new", expression = "java(toLinkedDataInstance(inventoryInstanceEvent))")
  public abstract ResourceIndexEvent toReindexEvent(InventoryInstanceEvent inventoryInstanceEvent);

  @Mapping(target = "suppress", source = "inventoryInstanceEvent")
  @Mapping(target = "id", source = "inventoryInstanceEvent.new.id")
  protected abstract LinkedDataInstance toLinkedDataInstance(InventoryInstanceEvent inventoryInstanceEvent);

  protected LinkedDataInstanceOnlySuppress extractSuppress(InventoryInstanceEvent event) {
    var newObj = event.getNew();
    if (ObjectUtils.allNull(newObj.getStaffSuppress(), newObj.getDiscoverySuppress())) {
      return null;
    }
    return new LinkedDataInstanceOnlySuppress()
      .staff(newObj.getStaffSuppress())
      .fromDiscovery(newObj.getDiscoverySuppress());
  }
}
