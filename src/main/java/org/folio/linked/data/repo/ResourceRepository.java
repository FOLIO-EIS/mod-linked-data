package org.folio.linked.data.repo;

import java.util.Optional;
import java.util.Set;
import org.folio.linked.data.model.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

  @Query("SELECT r FROM Resource r JOIN r.types t WHERE t.uri IN :types")
  Page<Resource> findAllByType(@Param("types") Set<String> types, Pageable pageable);

  @Query("SELECT r FROM Resource r JOIN r.types t WHERE r.indexDate IS NULL AND t.uri IN :types")
  Page<Resource> findNotIndexedByType(@Param("types") Set<String> types, Pageable pageable);

  @Modifying
  @Query("update Resource r set r.indexDate = current_timestamp() where r.id = :id")
  void updateIndexDate(@Param("id") Long id);

  @Modifying
  @Query("update Resource r set r.indexDate = current_timestamp() where r.id in (:ids)")
  void updateIndexDateBatch(@Param("ids") Set<Long> ids);

  Optional<Resource> findByActiveTrueAndFolioMetadataSrsId(String srsId);
}
