package org.folio.linked.data.mapper.kafka.inventory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.folio.linked.data.util.Constants.SEARCH_INSTANCE_RESOURCE_NAME;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.folio.linked.data.domain.dto.InventoryInstanceEvent;
import org.folio.linked.data.test.TestUtil;
import org.folio.spring.testing.type.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
public class InventoryInstanceMapperTest {

  private static final ObjectMapper MAPPER = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @InjectMocks
  private InventoryInstanceMapperImpl inventoryInstanceMapper;

  @Test
  void shouldMap() throws JsonProcessingException {
    // given
    var eventSource = TestUtil.loadResourceAsString("samples/inventoryInstanceEvent.json");
    var event = MAPPER.readValue(eventSource, InventoryInstanceEvent.class);

    // when
    var result = inventoryInstanceMapper.toReindexEvent(event);

    // then
    assertThat(result)
      .hasFieldOrPropertyWithValue("id", result.getId())
      .hasFieldOrPropertyWithValue("type", result.getType())
      .hasFieldOrPropertyWithValue("resourceName", SEARCH_INSTANCE_RESOURCE_NAME)
      .extracting("_new")
      .hasFieldOrPropertyWithValue("id", event.getNew().getId())
      .extracting("suppress")
      .hasFieldOrPropertyWithValue("staff", event.getNew().getStaffSuppress())
      .hasFieldOrPropertyWithValue("fromDiscovery", event.getNew().getDiscoverySuppress());
  }
}
