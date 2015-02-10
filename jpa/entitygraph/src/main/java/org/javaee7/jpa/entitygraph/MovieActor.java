package org.javaee7.jpa.entitygraph;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "MOVIE_ACTORS_ENTITY_GRAPH")
public class MovieActor implements Serializable {
    @Id
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String actor;

    @OneToMany
    @JoinColumn(name = "ID")
    private Set<MovieActorAward> movieActorAwards;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MovieActor that = (MovieActor) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
