package org.folio.linked.data.integration.kafka.listener.handler;

import static org.folio.linked.data.test.TestUtil.FOLIO_TEST_PROFILE;
import static org.folio.linked.data.test.TestUtil.TENANT_ID;
import static org.folio.linked.data.test.TestUtil.awaitAndAssert;
import static org.folio.linked.data.test.kafka.KafkaEventsTestDataFixture.getInventoryInstanceEventSampleProducerRecord;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.folio.linked.data.domain.dto.ResourceIndexEvent;
import org.folio.linked.data.e2e.base.IntegrationTest;
import org.folio.linked.data.repo.FolioMetadataRepository;
import org.folio.linked.data.service.tenant.TenantScopedExecutionService;
import org.folio.linked.data.test.kafka.KafkaInventoryInstanceTopicListener;
import org.folio.spring.tools.kafka.FolioMessageProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@IntegrationTest
@ActiveProfiles({FOLIO_PROFILE, FOLIO_TEST_PROFILE})
public class InventoryInstanceEventHandlerIT {

  @Autowired
  private KafkaTemplate<String, String> eventKafkaTemplate;
  @Autowired
  private TenantScopedExecutionService tenantScopedExecutionService;
  @Autowired
  private KafkaInventoryInstanceTopicListener kafkaSearchInventoryInstanceTopicListener;
  @MockBean
  private FolioMetadataRepository folioMetadataRepository;
  @Qualifier("instanceMessageProducer")
  @Autowired
  @SpyBean
  private FolioMessageProducer<ResourceIndexEvent> inventoryInstanceEventMessageProducer;

  @BeforeEach
  public void clean() {
    tenantScopedExecutionService.execute(TENANT_ID,
      () -> kafkaSearchInventoryInstanceTopicListener.getMessages().clear()
    );
  }

  @Test
  void shouldProcessAuthoritySourceRecordDomainCreateEvent() {
    // given
    var eventProducerRecord = getInventoryInstanceEventSampleProducerRecord();
    when(folioMetadataRepository.existsByInventoryId(any())).thenReturn(true);

    // when
    eventKafkaTemplate.send(eventProducerRecord);

    //then
    awaitAndAssert(() -> verify(inventoryInstanceEventMessageProducer).sendMessages(anyList()));
    awaitAndAssert(() -> assertFalse(kafkaSearchInventoryInstanceTopicListener.getMessages().isEmpty()));
  }
}
