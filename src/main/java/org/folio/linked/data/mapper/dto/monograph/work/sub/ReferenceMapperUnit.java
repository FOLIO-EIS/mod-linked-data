package org.folio.linked.data.mapper.dto.monograph.work.sub;

import static org.folio.linked.data.util.BibframeUtils.ensureLatestReplaced;
import static org.folio.linked.data.util.BibframeUtils.fetchResource;

import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import org.folio.linked.data.domain.dto.Reference;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.repo.ResourceRepository;

@RequiredArgsConstructor
public class ReferenceMapperUnit implements WorkSubResourceMapperUnit {

  private final BiConsumer<Reference, Object> referenceConsumer;
  private final ResourceRepository resourceRepository;

  @Override
  public <P> P toDto(Resource source, P parentDto, Resource parentResource) {
    source = ensureLatestReplaced(source);
    var reference = new Reference()
      .id(String.valueOf(source.getId()))
      .label(source.getLabel());
    referenceConsumer.accept(reference, parentDto);
    return parentDto;
  }

  @Override
  public Resource toEntity(Object dto, Resource parentEntity) {
    var reference = (Reference) dto;
    return fetchResource(reference, resourceRepository);
  }

}
