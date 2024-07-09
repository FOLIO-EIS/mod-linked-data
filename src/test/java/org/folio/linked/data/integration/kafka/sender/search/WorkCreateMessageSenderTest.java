package org.folio.linked.data.integration.kafka.sender.search;

import static java.lang.Long.parseLong;
import static org.assertj.core.api.Assertions.assertThat;
import static org.folio.ld.dictionary.ResourceTypeDictionary.WORK;
import static org.folio.linked.data.test.TestUtil.randomLong;
import static org.folio.search.domain.dto.ResourceIndexEventType.CREATE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.folio.linked.data.mapper.kafka.search.BibliographicSearchMessageMapper;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.model.entity.event.ResourceIndexedEvent;
import org.folio.search.domain.dto.LinkedDataWork;
import org.folio.search.domain.dto.ResourceIndexEvent;
import org.folio.spring.testing.type.UnitTest;
import org.folio.spring.tools.kafka.FolioMessageProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@UnitTest
@ExtendWith(MockitoExtension.class)
class WorkCreateMessageSenderTest {

  @InjectMocks
  private WorkCreateMessageSender producer;
  @Mock
  private ApplicationEventPublisher eventPublisher;
  @Mock
  private BibliographicSearchMessageMapper bibliographicSearchMessageMapper;
  @Mock
  private FolioMessageProducer<ResourceIndexEvent> resourceIndexEventMessageProducer;

  @Test
  void sendSingleWorkCreated_shouldNotSendMessage_ifGivenResourceIsNotIndexable() {
    // given
    var resource = new Resource().addTypes(WORK);
    when(bibliographicSearchMessageMapper.toIndex(resource, CREATE))
      .thenReturn(Optional.empty());

    // when
    producer.produce(resource);

    // then
    verifyNoInteractions(eventPublisher, resourceIndexEventMessageProducer);
  }

  @Test
  void sendSingleWorkCreated_shouldSendMessageAndPublishEvent_ifGivenResourceIsIndexable() {
    // given
    var resource = new Resource().addTypes(WORK).setId(randomLong());
    var index = new LinkedDataWork().id(String.valueOf(resource.getId()));
    when(bibliographicSearchMessageMapper.toIndex(resource, CREATE))
      .thenReturn(Optional.of(index));

    // when
    producer.produce(resource);

    // then
    var messageCaptor = ArgumentCaptor.forClass(List.class);
    verify(resourceIndexEventMessageProducer)
      .sendMessages(messageCaptor.capture());
    List<ResourceIndexEvent> messages = messageCaptor.getValue();
    var expectedIndexEvent = new ResourceIndexedEvent(parseLong(index.getId()));

    assertThat(messages)
      .singleElement()
      .hasFieldOrProperty("id")
      .hasFieldOrPropertyWithValue("type", CREATE)
      .hasFieldOrPropertyWithValue("resourceName", "linked-data-work")
      .hasFieldOrPropertyWithValue("_new", index);
    verify(eventPublisher)
      .publishEvent(expectedIndexEvent);
  }
}
