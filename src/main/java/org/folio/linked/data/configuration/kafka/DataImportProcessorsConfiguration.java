package org.folio.linked.data.configuration.kafka;

import static java.util.stream.Collectors.toMap;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import org.folio.linked.data.integration.kafka.consumer.DataImportEventProcessor;
import org.folio.search.domain.dto.DataImportEventSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(FOLIO_PROFILE)
public class DataImportProcessorsConfiguration {

  @Bean
  public Map<DataImportEventSource, DataImportEventProcessor<?>> dataImportEventProcessors(
    Collection<DataImportEventProcessor<?>> eventProcessors) {
    return eventProcessors.stream()
      .collect(toMap(DataImportEventProcessor::getEventSource, Function.identity()));
  }
}
