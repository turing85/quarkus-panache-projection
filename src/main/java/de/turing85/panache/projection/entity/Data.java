package de.turing85.panache.projection.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "public", name = "data")
@Getter
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Data extends PanacheEntityBase {
  @Id
  @SequenceGenerator(
      name = "DataIdGenerator",
      sequenceName = "data__seq__id",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DataIdGenerator")
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Override
  public boolean equals(Object thatObject) {
    if (this == thatObject) {
      return true;
    }
    if (Objects.isNull(thatObject) || getClass() != thatObject.getClass()) {
      return false;
    }
    Data that = (Data) thatObject;
    Long thisId = this.getId();
    Long thatId = that.getId();
    if (Objects.nonNull(thisId) && Objects.nonNull(thatId)) {
      return Objects.equals(thisId, thatId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
