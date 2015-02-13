package org.javaee7.jpa.entitygraph;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Roberto Cortez
 */
@Entity
@Table(name = "MOVIE_DIRECTORS_ENTITY_GRAPH")
public class MovieDirector {
    @Id
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    private String director;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieDirector that = (MovieDirector) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
