package org.folio.linked.data.mapper.dto.monograph.work.sub.creator;

import static org.folio.ld.dictionary.PredicateDictionary.CREATOR;
import static org.folio.ld.dictionary.ResourceTypeDictionary.MEETING;

import org.folio.linked.data.domain.dto.Agent;
import org.folio.linked.data.mapper.dto.common.MapperUnit;
import org.folio.linked.data.mapper.dto.monograph.work.sub.AgentRoleAssigner;
import org.folio.linked.data.repo.ResourceRepository;
import org.springframework.stereotype.Component;

@Component("CreatorMeetingMapperUnit")
@MapperUnit(type = MEETING, requestDto = Agent.class, predicate = CREATOR)
public class MeetingMapperUnit extends CreatorMapperUnit {

  public MeetingMapperUnit(AgentRoleAssigner agentRoleAssigner, ResourceRepository resourceRepository) {
    super(agentRoleAssigner, resourceRepository);
  }
}
