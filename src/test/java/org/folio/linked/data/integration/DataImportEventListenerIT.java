package org.folio.linked.data.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.folio.ld.dictionary.ResourceTypeDictionary.CONCEPT;
import static org.folio.ld.dictionary.ResourceTypeDictionary.INSTANCE;
import static org.folio.ld.dictionary.ResourceTypeDictionary.PERSON;
import static org.folio.linked.data.test.TestUtil.FOLIO_TEST_PROFILE;
import static org.folio.linked.data.test.TestUtil.TENANT_ID;
import static org.folio.linked.data.test.TestUtil.awaitAndAssert;
import static org.folio.linked.data.test.TestUtil.defaultKafkaHeaders;
import static org.folio.linked.data.test.TestUtil.loadResourceAsString;
import static org.folio.linked.data.test.kafka.KafkaEventsTestDataFixture.authorityEvent;
import static org.folio.linked.data.test.kafka.KafkaEventsTestDataFixture.instanceCreatedEvent;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;
import static org.folio.spring.tools.config.properties.FolioEnvironment.getFolioEnvName;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Objects;
import java.util.Set;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.folio.linked.data.e2e.base.IntegrationTest;
import org.folio.linked.data.integration.kafka.consumer.DataImportEventHandler;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.repo.ResourceEdgeRepository;
import org.folio.linked.data.repo.ResourceRepository;
import org.folio.linked.data.service.impl.tenant.TenantScopedExecutionService;
import org.folio.search.domain.dto.DataImportEvent;
import org.folio.spring.tools.kafka.KafkaAdminService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@IntegrationTest
@ActiveProfiles({FOLIO_PROFILE, FOLIO_TEST_PROFILE})
class DataImportEventListenerIT {

  private static final String DI_COMPLETED_TOPIC = "DI_COMPLETED";
  @Autowired
  private ResourceRepository resourceRepo;
  @Autowired
  private ResourceEdgeRepository resourceEdgeRepository;
  @Autowired
  private KafkaTemplate<String, String> eventKafkaTemplate;
  @SpyBean
  @Autowired
  private DataImportEventHandler dataImportEventHandler;
  @Autowired
  private TenantScopedExecutionService tenantScopedExecutionService;

  @BeforeAll
  static void beforeAll(@Autowired KafkaAdminService kafkaAdminService) {
    kafkaAdminService.createTopics(TENANT_ID);
  }

  private static String getTopicName(String tenantId, String topic) {
    return String.format("%s.%s.%s", getFolioEnvName(), tenantId, topic);
  }

  @BeforeEach
  public void clean() {
    resourceEdgeRepository.deleteAll();
    resourceRepo.deleteAll();
  }

  @Test
  void shouldConsumeInstanceCreatedEventFromDataImport() {
    // given
    var eventId = "event_id_01";
    var marc = loadResourceAsString("samples/full_marc_sample.jsonl");
    var emittedEvent = instanceCreatedEvent(eventId, TENANT_ID, marc);
    var expectedEvent = new DataImportEvent()
      .id(eventId)
      .tenant(TENANT_ID)
      .eventType("DI_COMPLETED")
      .marcBib(marc);
    var producerRecord = new ProducerRecord(getTopicName(TENANT_ID, DI_COMPLETED_TOPIC), 0,
      eventId, emittedEvent, defaultKafkaHeaders());

    // when
    eventKafkaTemplate.send(producerRecord);

    // then
    awaitAndAssert(() -> verify(dataImportEventHandler, times(1)).handle(expectedEvent));

    var found = tenantScopedExecutionService.execute(
      TENANT_ID,
      () -> resourceRepo.findAllByType(Set.of(INSTANCE.getUri()), Pageable.ofSize(1))
        .stream()
        .findFirst()
    );
    assertThat(found).isPresent();
    var result = found.get();
    assertThat(result.getLabel()).isEqualTo("Instance MainTitle");
    assertThat(result.getInventoryId()).hasToString("2165ef4b-001f-46b3-a60e-52bcdeb3d5a1");
    assertThat(result.getSrsId()).hasToString("43d58061-decf-4d74-9747-0e1c368e861b");
    assertThat(result.getTypes().iterator().next().getUri()).isEqualTo(INSTANCE.getUri());
    assertThat(result.getDoc()).isNotEmpty();
    assertThat(result.getOutgoingEdges()).isNotEmpty();
    result.getOutgoingEdges().forEach(edge -> {
      assertThat(edge.getSource()).isEqualTo(result);
      assertThat(edge.getTarget()).isNotNull();
      assertThat(edge.getPredicate()).isNotNull();
    });
  }

  @Test
  void shouldConsumeAuthorityEventFromDataImport() {
    // given
    var eventId = "event_id_01";
    var marc = loadResourceAsString("samples/authority_100.jsonl");
    var emittedEvent = authorityEvent(eventId, TENANT_ID, marc);
    var expectedEvent = new DataImportEvent()
      .id(eventId)
      .tenant(TENANT_ID)
      .eventType("DI_COMPLETED")
      .marcAuthority(marc);
    var producerRecord = new ProducerRecord(getTopicName(TENANT_ID, DI_COMPLETED_TOPIC), 0,
      eventId, emittedEvent, defaultKafkaHeaders());

    // when
    eventKafkaTemplate.send(producerRecord);

    // then
    awaitAndAssert(() -> verify(dataImportEventHandler, times(1)).handle(expectedEvent));

    var found = tenantScopedExecutionService.execute(
      TENANT_ID,
      () -> resourceRepo.findAllByType(Set.of(CONCEPT.getUri(), PERSON.getUri()), Pageable.ofSize(1))
        .stream()
        .findFirst()
    );

    assertThat(found)
      .isPresent()
      .get()
      .hasFieldOrPropertyWithValue("label",
        "bValue, aValue, cValue, qValue, dValue -- vValue -- xValue -- yValue -- zValue")
      .satisfies(resource -> assertThat(resource.getDoc()).isNotEmpty())
      .satisfies(resource -> assertThat(resource.getOutgoingEdges()).isNotEmpty())
      .extracting(Resource::getOutgoingEdges)
      .satisfies(resourceEdges -> assertThat(resourceEdges)
        .isNotEmpty()
        .allMatch(edge -> Objects.nonNull(edge.getSource()))
        .allMatch(edge -> Objects.nonNull(edge.getTarget()))
        .allMatch(edge -> Objects.nonNull(edge.getPredicate()))
      );
  }
}
