package org.folio.linked.data.configuration.json.deserialization;

import static org.folio.linked.data.util.BibframeConstants.INSTANCE;
import static org.folio.linked.data.util.Constants.DTO_UNKNOWN_SUB_ELEMENT;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import org.folio.linked.data.domain.dto.InstanceField;
import org.folio.linked.data.domain.dto.ResourceDto;
import org.folio.linked.data.domain.dto.ResourceField;
import org.folio.linked.data.exception.JsonException;

public class ResourceFieldDeserializer extends JsonDeserializer<ResourceField> {

  @Override
  public ResourceField deserialize(JsonParser jp, DeserializationContext deserializationContext)
    throws IOException {
    JsonNode node = jp.getCodec().readTree(jp);
    if (node.has(INSTANCE)) {
      return jp.getCodec().treeToValue(node, InstanceField.class);
    }
    var field = node.fieldNames().hasNext() ? node.fieldNames().next() : "";
    throw new JsonException(ResourceDto.class.getSimpleName() + DTO_UNKNOWN_SUB_ELEMENT + field);
  }
}