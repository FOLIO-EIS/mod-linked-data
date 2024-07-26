package org.folio.linked.data.integration.kafka.consumer;

import java.util.function.BiConsumer;
import org.folio.search.domain.dto.DataImportEvent;
import org.folio.search.domain.dto.DataImportEventSource;

public interface DataImportEventProcessor<R> extends BiConsumer<R, DataImportEvent> {

  R fromMarcJson(String marc);

  void process(DataImportEvent event);

  DataImportEventSource getEventSource();
}
