package org.folio.linked.data.util;

import static org.folio.linked.data.util.Constants.DTO_UNKNOWN_SUB_ELEMENT;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.folio.linked.data.exception.JsonException;

@RequiredArgsConstructor
public class DtoDeserializer<D> {

  private final Map<String, Class<? extends D>> identityMap;
  private final Class<D> dtoClass;

  public D deserialize(JsonParser jp)
    throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    return identityMap.entrySet()
      .stream()
      .filter(entry -> node.has(entry.getKey()))
      .map(Map.Entry::getValue)
      .map(clazz -> deserialize(jp, node, clazz))
      .findFirst()
      .orElseThrow(() -> createUnknownElementException(node));
  }


  private <T> T deserialize(JsonParser jp, JsonNode node, Class<T> clazz) {
    try {
      return jp.getCodec().treeToValue(node, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonException(e.getMessage(), e);
    }
  }

  private JsonException createUnknownElementException(JsonNode node) {
    var field = node.fieldNames().hasNext() ? node.fieldNames().next() : "";
    return new JsonException(dtoClass.getSimpleName() + DTO_UNKNOWN_SUB_ELEMENT + field);
  }
}
