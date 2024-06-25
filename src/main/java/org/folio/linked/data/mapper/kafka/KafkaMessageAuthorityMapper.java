package org.folio.linked.data.mapper.kafka;

import static java.util.Objects.nonNull;
import static org.folio.ld.dictionary.PredicateDictionary.MAP;
import static org.folio.ld.dictionary.PropertyDictionary.EAN_VALUE;
import static org.folio.ld.dictionary.PropertyDictionary.LOCAL_ID_VALUE;
import static org.folio.ld.dictionary.PropertyDictionary.NAME;
import static org.folio.ld.dictionary.ResourceTypeDictionary.CONCEPT;
import static org.folio.linked.data.util.BibframeUtils.isOfType;

import java.util.Optional;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.folio.linked.data.domain.dto.Instance;
import org.folio.linked.data.mapper.dto.common.SingleResourceMapper;
import org.folio.linked.data.model.entity.Resource;
import org.folio.search.domain.dto.BibframeAuthorityIdentifiersInner;
import org.folio.search.domain.dto.BibframeAuthorityIndex;
import org.folio.search.domain.dto.ResourceEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class KafkaMessageAuthorityMapper
  extends AbstractKafkaMessageMapper<BibframeAuthorityIndex, BibframeAuthorityIdentifiersInner> {

  @Autowired
  public KafkaMessageAuthorityMapper(SingleResourceMapper singleResourceMapper) {
    super(singleResourceMapper);
  }

  @Override
  public Optional<Long> toDeleteIndexId(@NonNull Resource resource) {
    log.debug("Delete index for authority isn't supported");
    return Optional.empty();
  }

  @Override
  public Optional<BibframeAuthorityIndex> toIndex(Resource resource, ResourceEventType eventType) {
    var indexDto = new BibframeAuthorityIndex()
      .id(parseId(resource))
      .label(resource.getLabel())
      .type(parseType(resource))
      .identifiers(extractIdentifiers(resource));
    return Optional.of(indexDto);
  }

  private String parseId(Resource resource) {
    return String.valueOf(resource.getId());
  }

  private String parseType(Resource resource) {
    var types = resource.getTypes();
    if (isOfType(resource, CONCEPT)) {
      return CONCEPT.toString();
    }
    return types.stream()
      .findFirst()
      .map(Object::toString)
      .orElse(StringUtils.EMPTY);
  }

  @Override
  protected Optional<BibframeAuthorityIdentifiersInner> mapToIdentifier(Resource resource) {
    var value = getValue(resource.getDoc(), NAME.getValue(), EAN_VALUE.getValue(), LOCAL_ID_VALUE.getValue());
    var type = toType(resource, BibframeAuthorityIdentifiersInner.TypeEnum::fromValue,
      BibframeAuthorityIdentifiersInner.TypeEnum.class, MAP, Instance.class);
    return Optional.of(new BibframeAuthorityIdentifiersInner())
      .map(i -> i.value(value))
      .map(i -> i.type(type))
      .filter(i -> nonNull(i.getValue()));
  }
}