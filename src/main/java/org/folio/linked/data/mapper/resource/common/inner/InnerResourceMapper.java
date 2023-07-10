package org.folio.linked.data.mapper.resource.common.inner;

import org.folio.linked.data.domain.dto.BibframeResponse;
import org.folio.linked.data.model.entity.Resource;

public interface InnerResourceMapper {

  BibframeResponse toDto(Resource source, BibframeResponse destination);

  Resource toEntity(Object dto, String resourceType);
}