package org.folio.linked.data.integration.kafka.consumer;

import static java.util.Optional.ofNullable;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.search.domain.dto.DataImportEvent;
import org.folio.search.domain.dto.DataImportEventSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@Profile(FOLIO_PROFILE)
@RequiredArgsConstructor
public class DataImportEventHandler {

  private final Map<DataImportEventSource, DataImportEventProcessor<?>> eventProcessors;

  public void handle(DataImportEvent event) {
    ofNullable(eventProcessors.get(event.getEventSource()))
      .ifPresentOrElse(p -> p.process(event),
        () -> log.error("DataImportEvent with id [{}], tenant [{}], eventType [{}] has no Marc record inside",
          event.getId(), event.getTenant(), event.getEventType()));
  }
}
