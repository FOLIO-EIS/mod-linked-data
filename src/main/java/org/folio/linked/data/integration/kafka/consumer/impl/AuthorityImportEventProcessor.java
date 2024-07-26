package org.folio.linked.data.integration.kafka.consumer.impl;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.ld.dictionary.model.Resource;
import org.folio.linked.data.integration.kafka.consumer.AbstractDataImportEventProcessor;
import org.folio.marc4ld.service.marc2ld.authority.MarcAuthority2ldMapper;
import org.folio.search.domain.dto.DataImportEvent;
import org.folio.search.domain.dto.DataImportEventSource;

@Log4j2
@AllArgsConstructor
public class AuthorityImportEventProcessor
  extends AbstractDataImportEventProcessor<Collection<Resource>, MarcAuthority2ldMapper> {

  @Override
  public void accept(Collection<Resource> resources, DataImportEvent event) {
    var ids = resources.stream()
      .map(resourceService::createResource)
      .toList();
    log.info("Processing MARC Authority record with event ID [{}], saved records: {}",
      event.getId(), ids.size());
  }

  @Override
  public DataImportEventSource getEventSource() {
    return DataImportEventSource.AUTHORITY;
  }
}
