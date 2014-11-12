package org.javaee7.jpa.entitygraph;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "MOVIE_ENTITY_GRAPH")
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m")
})
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "movieWithActors",
        attributeNodes = {
            @NamedAttributeNode("movieActors")
        }
    ),
    @NamedEntityGraph(
        name = "movieWithActorsAndAwards",
        attributeNodes = {
            @NamedAttributeNode(value = "movieActors", subgraph = "movieActorsGraph")
        },
        subgraphs = {
            @NamedSubgraph(
                    name = "movieActorsGraph",
                    attributeNodes = {
                        @NamedAttributeNode("movieActorAwards")
                    }
            )
        }
    )
})
public class Movie implements Serializable {
    @Id
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @OneToMany
    @JoinColumn(name = "ID")
    private List<MovieActor> movieActors;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID")
    private List<MovieDirector> movieDirectors;

    @OneToMany
    @JoinColumn(name = "ID")
    private List<MovieAward> movieAwards;

    public List<MovieActor> getMovieActors() {
        return movieActors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
