package org.folio.linked.data.mapper;

import static org.folio.ld.dictionary.PredicateDictionary.PROVIDER_PLACE;
import static org.folio.ld.dictionary.ResourceTypeDictionary.PROVIDER_EVENT;
import static org.folio.ld.dictionary.ResourceTypeDictionary.WORK;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.folio.linked.data.mapper.resource.kafka.KafkaMessageMapper;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.model.entity.ResourceEdge;
import org.folio.linked.data.model.entity.ResourceTypeEntity;
import org.folio.spring.test.type.UnitTest;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@UnitTest
@ExtendWith(MockitoExtension.class)
class ResourceMapperTest {

  @InjectMocks
  private ResourceMapperImpl resourceMapper;

  @Mock
  private KafkaMessageMapper kafkaMessageMapper;

  @Test
  void mapToIndex_shouldThrowNullPointerException_ifGivenResourceIsNull() {
    // given
    Resource resource = null;

    // when
    var thrown = assertThrows(NullPointerException.class, () -> resourceMapper.mapToIndex(resource));

    // then
    MatcherAssert.assertThat(thrown.getMessage(), is("resource is marked non-null but is null"));
  }

  @Test
  void mapToIndex_shouldCallKafkaMessageMapper_ifGivenResourceIsNotNull() {
    // given
    var resource = new Resource().addType(WORK);

    // when
    resourceMapper.mapToIndex(resource);

    // then
    verify(kafkaMessageMapper).toIndex(resource);
  }

  @Test
  void toResourceGraphDto_shouldReturnResourceGraphDto() {
    //given
    var resource = new Resource()
      .setResourceHash(3856321131L)
      .setTypes(Set.of(new ResourceTypeEntity().setUri(PROVIDER_EVENT.getUri())))
      .setDoc(new ObjectMapper().valueToTree(Map.of(
        "http://bibfra.me/vocab/lite/name", List.of("name $ 2023"),
        "http://bibfra.me/vocab/lite/providerDate", List.of("1981"),
        "http://bibfra.me/vocab/lite/place", List.of("Alaska"))))
      .setLabel("Alaska")
      .setIndexDate(Timestamp.valueOf(LocalDateTime.parse("2018-05-05T11:50:55")));
    var providerPlace = new Resource().setResourceHash(1654360880L);
    resource.setOutgoingEdges(Set.of(new ResourceEdge(resource, providerPlace, PROVIDER_PLACE)));

    //when
    var resourceGraphDto = resourceMapper.toResourceGraphDto(resource);

    //then
    assertEquals(resource.getResourceHash().toString(), resourceGraphDto.getId());
    assertEquals(resource.getTypes().iterator().next().getUri(), resourceGraphDto.getTypes().get(0));
    assertEquals(resource.getDoc(), resourceGraphDto.getDoc());
    assertEquals(resource.getLabel(), resourceGraphDto.getLabel());
    assertEquals(String.valueOf(resource.getOutgoingEdges().iterator().next().getTarget().getResourceHash()),
      ((Map<String, List<String>>) resourceGraphDto.getOutgoingEdges())
        .get(resource.getOutgoingEdges().iterator().next().getPredicate().getUri()).get(0));
    assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").format(resource.getIndexDate()),
      resourceGraphDto.getIndexDate());
  }
}
