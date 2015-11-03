package org.javaee7.jpa.entitygraph;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Roberto Cortez
 */
@Entity
@Table(name = "MOVIE_ACTOR_AWARDS_ENTITY_GRAPH")
public class MovieActorAward {
    @Id
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    private String award;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MovieActorAward that = (MovieActorAward) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
