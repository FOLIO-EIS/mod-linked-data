package org.folio.linked.data.integration.kafka.listener;

import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.isNotBlank;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;
import static org.folio.spring.integration.XOkapiHeaders.TENANT;
import static org.folio.spring.integration.XOkapiHeaders.URL;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Headers;
import org.apache.logging.log4j.Level;
import org.folio.linked.data.integration.kafka.consumer.DataImportEventHandler;
import org.folio.linked.data.service.impl.tenant.TenantScopedExecutionService;
import org.folio.search.domain.dto.DataImportEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.retry.RetryContext;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@Profile(FOLIO_PROFILE)
@RequiredArgsConstructor
public class KafkaMessageListener {

  private static final String DI_INSTANCE_CREATED_LISTENER = "mod-linked-data-data-import-instance-created-listener";
  private static final Set<String> REQUIRED_FOLIO_HEADERS = Set.of(TENANT, URL);
  private final TenantScopedExecutionService tenantScopedExecutionService;
  private final DataImportEventHandler dataImportEventHandler;

  @KafkaListener(
    id = DI_INSTANCE_CREATED_LISTENER,
    containerFactory = "dataImportListenerContainerFactory",
    groupId = "#{folioKafkaProperties.listener['data-import-instance-create'].groupId}",
    concurrency = "#{folioKafkaProperties.listener['data-import-instance-create'].concurrency}",
    topicPattern = "#{folioKafkaProperties.listener['data-import-instance-create'].topicPattern}")
  public void handleDataImportInstanceCreatedEvent(List<ConsumerRecord<String, DataImportEvent>> consumerRecords) {
    consumerRecords.forEach(this::processRecord);
  }

  private void processRecord(ConsumerRecord<String, DataImportEvent> consumerRecord) {
    log.info("Received event: {}", consumerRecord);
    var event = consumerRecord.value();
    if (isNotContainsRequiredHeaders(consumerRecord.headers())) {
      log.warn("Received DataImportEvent with no required Folio headers will be ignored: {}", event);
      return;
    }
    tenantScopedExecutionService.executeAsyncWithRetry(
      consumerRecord.headers(),
      retryContext -> runRetryableJob(event, retryContext),
      ex -> logFailedEvent(event, ex, false)
    );
  }

  private void runRetryableJob(DataImportEvent event, RetryContext context) {
    ofNullable(context.getLastThrowable())
      .ifPresent(ex -> logFailedEvent(event, ex, true));
    dataImportEventHandler.handle(event);
  }

  private void logFailedEvent(DataImportEvent event, Throwable ex, boolean isRetrying) {
    String marcRecord = isNotBlank(event.getMarcBib()) ? event.getMarcBib() : event.getMarcAuthority();
    String type = isNotBlank(event.getMarcBib()) ? "Bib" : "Authority";
    var logLevel = isRetrying ? Level.INFO : Level.ERROR;
    log.log(logLevel, "Failed to process MARC {} record {}. Retrying: {}", type, marcRecord, isRetrying, ex);
  }

  private boolean isNotContainsRequiredHeaders(Headers headers) {
    return !REQUIRED_FOLIO_HEADERS.stream()
      .map(required -> headers.headers(required).iterator())
      .allMatch(iterator -> iterator.hasNext() && iterator.next().value().length > 0);
  }
}
