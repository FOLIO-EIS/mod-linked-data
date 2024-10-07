package org.folio.linked.data.service.impl;

import static org.folio.marc4ld.util.MarcUtil.isLanguageMaterial;
import static org.folio.marc4ld.util.MarcUtil.isMonographicComponentPartOrItem;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.folio.linked.data.client.SrsClient;
import org.folio.linked.data.service.MarcService;
import org.folio.rest.jaxrs.model.Record;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MarcServiceImpl implements MarcService {

  private final SrsClient srsClient;

  @Override
  public Boolean isSupportedByInventoryId(String inventoryId) {
    var response = srsClient.getSourceStorageRecordFormattedById(inventoryId);
    return Optional.ofNullable(response.getBody())
      .map(Record::getParsedRecord)
      .map(parsedRecord -> (Map<?, ?>) parsedRecord.getContent())
      .map(content -> (String) content.get("leader"))
      .map(this::isMonograph)
      .orElse(false);
  }

  private boolean isMonograph(String leader) {
    return isLanguageMaterial(leader.charAt(6)) && isMonographicComponentPartOrItem(leader.charAt(7));
  }
}
