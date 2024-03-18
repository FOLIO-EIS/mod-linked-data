package org.folio.linked.data.mapper.dto.monograph.work;

import static java.util.Optional.ofNullable;
import static org.folio.ld.dictionary.PredicateDictionary.CLASSIFICATION;
import static org.folio.ld.dictionary.PredicateDictionary.CONTENT;
import static org.folio.ld.dictionary.PredicateDictionary.CONTRIBUTOR;
import static org.folio.ld.dictionary.PredicateDictionary.CREATOR;
import static org.folio.ld.dictionary.PredicateDictionary.GEOGRAPHIC_COVERAGE;
import static org.folio.ld.dictionary.PredicateDictionary.GOVERNMENT_PUBLICATION;
import static org.folio.ld.dictionary.PredicateDictionary.INSTANTIATES;
import static org.folio.ld.dictionary.PredicateDictionary.SUBJECT;
import static org.folio.ld.dictionary.PredicateDictionary.TARGET_AUDIENCE;
import static org.folio.ld.dictionary.PropertyDictionary.BIBLIOGRAPHY_NOTE;
import static org.folio.ld.dictionary.PropertyDictionary.DATE_END;
import static org.folio.ld.dictionary.PropertyDictionary.DATE_START;
import static org.folio.ld.dictionary.PropertyDictionary.LANGUAGE;
import static org.folio.ld.dictionary.PropertyDictionary.LANGUAGE_NOTE;
import static org.folio.ld.dictionary.PropertyDictionary.NOTE;
import static org.folio.ld.dictionary.PropertyDictionary.RESPONSIBILITY_STATEMENT;
import static org.folio.ld.dictionary.PropertyDictionary.SUMMARY;
import static org.folio.ld.dictionary.PropertyDictionary.TABLE_OF_CONTENTS;
import static org.folio.ld.dictionary.ResourceTypeDictionary.WORK;
import static org.folio.linked.data.util.BibframeUtils.putProperty;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.folio.ld.dictionary.PropertyDictionary;
import org.folio.linked.data.domain.dto.ResourceDto;
import org.folio.linked.data.domain.dto.Work;
import org.folio.linked.data.domain.dto.WorkField;
import org.folio.linked.data.mapper.dto.common.CoreMapper;
import org.folio.linked.data.mapper.dto.common.MapperUnit;
import org.folio.linked.data.mapper.dto.common.SingleResourceMapperUnit;
import org.folio.linked.data.mapper.dto.monograph.common.NoteMapper;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.service.HashService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@MapperUnit(type = WORK, dtoClass = WorkField.class)
public class WorkMapperUnit implements SingleResourceMapperUnit {

  private static final Set<PropertyDictionary> SUPPORTED_NOTES = Set.of(BIBLIOGRAPHY_NOTE, LANGUAGE_NOTE, NOTE);
  private static final Set<Class<?>> SUPPORTED_PARENTS = Collections.singleton(ResourceDto.class);
  private final CoreMapper coreMapper;
  private final NoteMapper noteMapper;
  private final HashService hashService;

  @Override
  public <P> P toDto(Resource source, P parentDto, Resource parentResource) {
    if (parentDto instanceof ResourceDto resourceDto) {
      var work = coreMapper.toDtoWithEdges(source, Work.class, true);
      work.setId(String.valueOf(source.getResourceHash()));
      ofNullable(source.getDoc()).ifPresent(doc -> work.setNotes(noteMapper.toNotes(doc, SUPPORTED_NOTES)));
      resourceDto.setResource(new WorkField().work(work));
    }
    return parentDto;
  }

  @Override
  public Resource toEntity(Object dto, Resource parentEntity) {
    var workDto = ((WorkField) dto).getWork();
    var work = new Resource();
    work.addType(WORK);
    work.setDoc(getDoc(workDto));
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getClassification(), CLASSIFICATION);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getContent(), CONTENT);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getSubjects(), SUBJECT);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getCreator(), CREATOR);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getContributor(), CONTRIBUTOR);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getGeographicCoverageReference(), GEOGRAPHIC_COVERAGE);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getGovernmentPublication(), GOVERNMENT_PUBLICATION);
    coreMapper.addOutgoingEdges(work, Work.class, workDto.getTargetAudience(), TARGET_AUDIENCE);
    coreMapper.addIncomingEdges(work, Work.class, workDto.getInstanceReference(), INSTANTIATES);
    work.setResourceHash(hashService.hash(work));
    return work;
  }

  private JsonNode getDoc(Work dto) {
    var map = new HashMap<String, List<String>>();
    putProperty(map, RESPONSIBILITY_STATEMENT, dto.getResponsibiltyStatement());
    putProperty(map, LANGUAGE, dto.getLanguage());
    putProperty(map, SUMMARY, dto.getSummary());
    putProperty(map, TABLE_OF_CONTENTS, dto.getTableOfContents());
    putProperty(map, DATE_START, dto.getDateStart());
    putProperty(map, DATE_END, dto.getDateEnd());
    noteMapper.putNotes(dto.getNotes(), map);
    return map.isEmpty() ? null : coreMapper.toJson(map);
  }

  @Override
  public Set<Class<?>> supportedParents() {
    return SUPPORTED_PARENTS;
  }

}