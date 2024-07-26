package org.folio.linked.data.integration.kafka.consumer;

import org.folio.linked.data.service.ResourceService;
import org.folio.marc4ld.service.marc2ld.MarcJson2ldMapper;
import org.folio.search.domain.dto.DataImportEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractDataImportEventProcessor<R, M extends MarcJson2ldMapper<R>>
  implements DataImportEventProcessor<R> {

  @Autowired
  protected ResourceService resourceService;

  @Autowired
  private M mapper;

  @Override
  public void process(DataImportEvent event) {
    accept(fromMarcJson(event.getPayload()), event);
  }

  @Override
  public R fromMarcJson(String marc) {
    return mapper.fromMarcJson(marc);
  }
}
