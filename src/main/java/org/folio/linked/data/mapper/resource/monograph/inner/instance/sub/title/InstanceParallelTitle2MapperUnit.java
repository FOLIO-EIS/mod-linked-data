package org.folio.linked.data.mapper.resource.monograph.inner.instance.sub.title;

import static org.folio.linked.data.util.Bibframe2Constants.DATE_URL;
import static org.folio.linked.data.util.Bibframe2Constants.INSTANCE_TITLE_2_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.MAIN_TITLE_URL;
import static org.folio.linked.data.util.Bibframe2Constants.NOTE_PRED;
import static org.folio.linked.data.util.Bibframe2Constants.NOTE_URL;
import static org.folio.linked.data.util.Bibframe2Constants.PARALLEL_TITLE_2;
import static org.folio.linked.data.util.Bibframe2Constants.PARALLEL_TITLE_URL;
import static org.folio.linked.data.util.Bibframe2Constants.PART_NAME_URL;
import static org.folio.linked.data.util.Bibframe2Constants.PART_NUMBER_URL;
import static org.folio.linked.data.util.Bibframe2Constants.SUBTITLE_URL;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.folio.linked.data.domain.dto.Instance2;
import org.folio.linked.data.domain.dto.ParallelTitle2;
import org.folio.linked.data.domain.dto.ParallelTitleField2;
import org.folio.linked.data.mapper.resource.common.CoreMapper;
import org.folio.linked.data.mapper.resource.common.MapperUnit;
import org.folio.linked.data.mapper.resource.common.inner.sub.SubResourceMapper;
import org.folio.linked.data.mapper.resource.monograph.inner.common.Note2MapperUnit;
import org.folio.linked.data.mapper.resource.monograph.inner.instance.sub.Instance2SubResourceMapperUnit;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.model.entity.ResourceType;
import org.folio.linked.data.service.dictionary.DictionaryService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(type = PARALLEL_TITLE_URL, predicate = INSTANCE_TITLE_2_PRED, dtoClass = ParallelTitleField2.class)
public class InstanceParallelTitle2MapperUnit implements Instance2SubResourceMapperUnit {

  private final DictionaryService<ResourceType> resourceTypeService;
  private final CoreMapper coreMapper;
  private final Note2MapperUnit<ParallelTitle2> noteMapper;

  @Override
  public Instance2 toDto(Resource source, Instance2 destination) {
    var parallelTitle = coreMapper.readResourceDoc(source, ParallelTitle2.class);
    coreMapper.addMappedResources(noteMapper, source, NOTE_PRED, parallelTitle);
    destination.addTitleItem(new ParallelTitleField2().parallelTitle(parallelTitle));
    return destination;
  }

  @Override
  public Resource toEntity(Object dto, String predicate, SubResourceMapper subResourceMapper) {
    var parallelTitle = ((ParallelTitleField2) dto).getParallelTitle();
    var resource = new Resource();
    resource.setLabel(PARALLEL_TITLE_URL);
    resource.addType(resourceTypeService.get(PARALLEL_TITLE_2));
    resource.setDoc(getDoc(parallelTitle));
    coreMapper.mapResourceEdges(parallelTitle.getNote(), resource, NOTE_URL, NOTE_PRED,
      (fieldDto, pred) -> noteMapper.toEntity(fieldDto, pred, null));
    resource.setResourceHash(coreMapper.hash(resource));
    return resource;
  }

  private JsonNode getDoc(ParallelTitle2 dto) {
    var map = new HashMap<String, List<String>>();
    map.put(PART_NAME_URL, dto.getPartName());
    map.put(PART_NUMBER_URL, dto.getPartNumber());
    map.put(MAIN_TITLE_URL, dto.getMainTitle());
    map.put(DATE_URL, dto.getDate());
    map.put(SUBTITLE_URL, dto.getSubtitle());
    return coreMapper.toJson(map);
  }

}
