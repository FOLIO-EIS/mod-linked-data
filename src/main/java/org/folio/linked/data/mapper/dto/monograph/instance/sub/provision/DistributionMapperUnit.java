package org.folio.linked.data.mapper.dto.monograph.instance.sub.provision;

import static org.folio.ld.dictionary.PredicateDictionary.PE_DISTRIBUTION;
import static org.folio.ld.dictionary.ResourceTypeDictionary.PROVIDER_EVENT;

import org.folio.linked.data.domain.dto.ProviderEventRequest;
import org.folio.linked.data.mapper.dto.common.CoreMapper;
import org.folio.linked.data.mapper.dto.common.MapperUnit;
import org.folio.linked.data.service.resource.hash.HashService;
import org.springframework.stereotype.Component;

@Component
@MapperUnit(type = PROVIDER_EVENT, predicate = PE_DISTRIBUTION, requestDto = ProviderEventRequest.class)
public class DistributionMapperUnit extends ProviderEventMapperUnit {

  public DistributionMapperUnit(CoreMapper coreMapper, HashService hashService) {
    super(coreMapper, hashService, (providerEvent, instance) -> instance.addDistributionItem(providerEvent));
  }
}
