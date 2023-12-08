package org.folio.linked.data.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.folio.linked.data.test.TestUtil.FOLIO_TEST_PROFILE;
import static org.folio.linked.data.test.TestUtil.TENANT_ID;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;
import static org.folio.linked.data.util.Constants.SEARCH_PROFILE;
import static org.folio.search.domain.dto.ResourceEventType.CREATE;
import static org.folio.search.domain.dto.ResourceEventType.DELETE;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;
import static org.testcontainers.shaded.org.awaitility.Durations.FIVE_SECONDS;

import lombok.SneakyThrows;
import org.folio.linked.data.repo.ResourceRepository;
import org.folio.linked.data.service.tenant.TenantScopedExecutionService;
import org.folio.linked.data.test.kafka.KafkaSearchIndexTopicListener;
import org.folio.spring.tools.kafka.KafkaAdminService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({FOLIO_PROFILE, FOLIO_TEST_PROFILE, SEARCH_PROFILE})
public class ResourceControllerFolioIT extends ResourceControllerIT {

  @Autowired
  private KafkaSearchIndexTopicListener consumer;
  @Autowired
  private TenantScopedExecutionService tenantScopedExecutionService;
  @Autowired
  private ResourceRepository resourceRepository;

  @BeforeAll
  static void beforeAll(@Autowired KafkaAdminService kafkaAdminService) {
    kafkaAdminService.createTopics(TENANT_ID);
  }

  @BeforeEach
  public void clean() {
    consumer.getMessages().clear();
    tenantScopedExecutionService.executeTenantScoped(TENANT_ID, super::clean);
  }

  @SneakyThrows
  @Override
  protected void checkKafkaMessageSentAndMarkedAsIndexed(Long id, boolean createOrDelete) {
    await().pollDelay(FIVE_SECONDS).untilAsserted(() ->
      assertTrue(consumer.getMessages().stream().anyMatch(m -> m.contains(id.toString())
        && m.contains(createOrDelete ? CREATE.getValue() : DELETE.getValue())))
    );
    if (createOrDelete) {
      var freshPersistedOptional = resourceRepository.findById(id);
      assertThat(freshPersistedOptional).isPresent();
      var freshPersisted = freshPersistedOptional.get();
      assertThat(freshPersisted.getIndexDate()).isNotNull();
    }
  }

}
