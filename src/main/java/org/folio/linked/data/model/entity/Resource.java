package org.folio.linked.data.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "resources")
@NoArgsConstructor
@Getter
@Setter
public class Resource {

  @Id
  private Long resourceHash;

  @NonNull
  @Column(nullable = false)
  private String label;

  @Column(columnDefinition = "json")
  @Type(JsonBinaryType.class)
  private JsonNode doc;

  @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
  private Set<ResourceEdge> outgoingEdges = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "type_hash")
  private ResourceType type;


}