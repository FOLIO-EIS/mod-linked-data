package org.folio.linked.data.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "instance_metadata")
@EqualsAndHashCode(of = "id")
public class InstanceMetadata {

  @Id
  @Column(name = "resource_hash")
  private Long id;

  @Column(name = "inventory_id")
  private UUID inventoryId;

  @Column(name = "srs_id")
  private UUID srsId;

  @Column(name = "source")
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private ResourceSource source;

  @OneToOne
  @MapsId
  @JoinColumn(name = "resource_hash")
  @ToString.Exclude
  private Resource resource;
}
