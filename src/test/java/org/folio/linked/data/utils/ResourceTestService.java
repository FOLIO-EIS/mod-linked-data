package org.folio.linked.data.utils;

import java.util.stream.Stream;
import org.folio.linked.data.exception.NotFoundException;
import org.folio.linked.data.model.entity.Resource;
import org.folio.linked.data.repo.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for testing purpose.
 */
@Service
public class ResourceTestService {
  @Autowired
  private ResourceRepository resourceRepository;

  /**
   * Retrieves a resource by its unique identifier along with its associated edges up to a specified depth.
   */
  @Transactional(readOnly = true)
  public Resource getResourceById(String id, int edgesDepth) {
    return resourceRepository.findById(Long.parseLong(id))
      .map(resource -> {
        fetchEdges(resource, edgesDepth);
        return resource;
      })
      .orElseThrow(() -> new NotFoundException("Resource not found by id: " + id));
  }

  private void fetchEdges(Resource resource, int edgesDepth) {
    if (edgesDepth <= 0) {
      return;
    }
    Stream.concat(resource.getIncomingEdges().stream(), resource.getOutgoingEdges().stream())
      .forEach(edge -> {
        fetchEdges(edge.getSource(), edgesDepth - 1);
        fetchEdges(edge.getTarget(), edgesDepth - 1);
      });
  }
}