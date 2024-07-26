package org.folio.linked.data.integration.kafka.consumer.impl;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.folio.ld.dictionary.model.Resource;
import org.folio.linked.data.integration.kafka.consumer.AbstractDataImportEventProcessor;
import org.folio.marc4ld.service.marc2ld.bib.MarcBib2ldMapper;
import org.folio.search.domain.dto.DataImportEvent;
import org.folio.search.domain.dto.DataImportEventSource;

@Log4j2
@AllArgsConstructor
public class BibImportEventProcessor extends AbstractDataImportEventProcessor<Optional<Resource>, MarcBib2ldMapper> {

  @Override
  public void accept(Optional<Resource> resource, DataImportEvent event) {
    resource.ifPresentOrElse(r -> {
        var id = resourceService.createResource(r);
        log.info("DataImportEvent with id [{}] was saved as LD resource with id [{}]", event.getId(), id);
      },
      () -> log.warn("Empty resource from MARC for event ID [{}]", event.getId())
    );
  }

  @Override
  public DataImportEventSource getEventSource() {
    return DataImportEventSource.BIBLIOGRAPHIC;
  }
}
