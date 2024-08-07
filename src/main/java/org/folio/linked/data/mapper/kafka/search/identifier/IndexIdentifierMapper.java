package org.folio.linked.data.mapper.kafka.search.identifier;

import java.util.List;
import org.folio.linked.data.model.entity.Resource;

public interface IndexIdentifierMapper<T> {

  List<T> extractIdentifiers(Resource resource);
}
