package org.folio.linked.data.integration.kafka.sender.inventory;

import static org.folio.linked.data.util.BibframeUtils.extractInstances;
import static org.folio.linked.data.util.Constants.FOLIO_PROFILE;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.folio.linked.data.integration.kafka.sender.UpdateMessageSender;
import org.folio.linked.data.mapper.kafka.inventory.KafkaInventoryMessageMapper;
import org.folio.linked.data.model.entity.Resource;
import org.folio.search.domain.dto.InstanceIngressEvent;
import org.folio.spring.tools.kafka.FolioMessageProducer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(FOLIO_PROFILE)
@RequiredArgsConstructor
public class InstanceUpdateMessageSender implements UpdateMessageSender {

  private final KafkaInventoryMessageMapper kafkaInventoryMessageMapper;
  private final FolioMessageProducer<InstanceIngressEvent> instanceIngressMessageProducer;

  @Override
  public Collection<Resource> apply(Resource resource) {
    return extractInstances(resource);
  }

  @Override
  public void accept(Resource resource) {
    var message = kafkaInventoryMessageMapper.toInstanceIngressEvent(resource);
    instanceIngressMessageProducer.sendMessages(List.of(message));
  }

}
