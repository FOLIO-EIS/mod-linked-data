package org.folio.linked.data.mapper.resource.monograph.inner.work.sub;

import static org.folio.linked.data.util.BibframeConstants.GEOGRAPHIC_COVERAGE_PRED;
import static org.folio.linked.data.util.BibframeConstants.SAME_AS_PRED;

import lombok.RequiredArgsConstructor;
import org.folio.linked.data.domain.dto.GeographicCoverage;
import org.folio.linked.data.domain.dto.GeographicCoverageField;
import org.folio.linked.data.domain.dto.Work;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(predicate = GEOGRAPHIC_COVERAGE_PRED)
public class GeoCoverageMapperUnit implements WorkSubResourceMapperUnit {

  private final CoreMapper coreMapper;

  @Override
  public Work toDto(Resource source, Work destination) {
    var geoCoverage = coreMapper.readResourceDoc(source, GeographicCoverage.class);
    coreMapper.addMappedProperties(source, SAME_AS_PRED, geoCoverage::addSameAsItem);
    destination.addGeographicCoverageItem(new GeographicCoverageField().geographicCoverage(geoCoverage));
    return destination;
  }
}