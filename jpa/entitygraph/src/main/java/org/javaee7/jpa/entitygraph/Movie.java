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
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.findAllById", query = "SELECT m FROM Movie m WHERE m.id = :movieId"),
    @NamedQuery(name = "Movie.findAllByIds", query = "SELECT m FROM Movie m WHERE m.id IN :movieIds")
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
    private Set<MovieActor> movieActors;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID")
    private Set<MovieDirector> movieDirectors;

    @OneToMany
    @JoinColumn(name = "ID")
    private Set<MovieAward> movieAwards;

    public Integer getId() {
        return id;
    }

    public Set<MovieActor> getMovieActors() {
        return movieActors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Movie movie = (Movie) o;

        return id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
