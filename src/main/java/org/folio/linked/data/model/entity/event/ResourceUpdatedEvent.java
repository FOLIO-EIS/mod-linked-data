package org.folio.linked.data.model.entity.event;

import org.folio.linked.data.model.entity.Resource;

public record ResourceUpdatedEvent(Resource resource) implements ResourceEvent {
}
