package org.folio.linked.data.mapper.kafka.inventory;

import static org.folio.linked.data.util.Constants.SEARCH_INSTANCE_RESOURCE_NAME;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.util.UUID;
import org.apache.commons.lang3.ObjectUtils;
import org.folio.linked.data.domain.dto.InventoryInstance;
import org.folio.linked.data.domain.dto.InventoryInstanceEvent;
import org.folio.linked.data.domain.dto.LinkedDataInstance;
import org.folio.linked.data.domain.dto.LinkedDataInstanceOnlySuppress;
import org.folio.linked.data.domain.dto.ResourceIndexEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = SPRING, imports = UUID.class)
public abstract class InventoryInstanceMapper {

  @Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
  @Mapping(target = "resourceName", constant = SEARCH_INSTANCE_RESOURCE_NAME)
  @Mapping(target = "_new", expression = "java(toLinkedDataInstance(inventoryInstanceEvent.getNew()))")
  @Mapping(target = "old", expression = "java(toLinkedDataInstance(inventoryInstanceEvent.getOld()))")
  public abstract ResourceIndexEvent toReindexEvent(InventoryInstanceEvent inventoryInstanceEvent);

  @Mapping(target = "suppress", source = "inventoryInstance", qualifiedByName = "extractSuppress")
  @Mapping(target = "id", source = "inventoryInstance.id")
  protected abstract LinkedDataInstance toLinkedDataInstance(InventoryInstance inventoryInstance);

  @Named("extractSuppress")
  protected LinkedDataInstanceOnlySuppress extractSuppress(InventoryInstance inventoryInstance) {
    if (ObjectUtils.allNull(inventoryInstance.getStaffSuppress(), inventoryInstance.getDiscoverySuppress())) {
      return null;
    }
    return new LinkedDataInstanceOnlySuppress()
      .staff(inventoryInstance.getStaffSuppress())
      .fromDiscovery(inventoryInstance.getDiscoverySuppress());
  }
}
