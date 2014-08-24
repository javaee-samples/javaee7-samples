package org.javaee7.jpa.entitygraph;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "MOVIE_ACTORS_ENTITY_GRAPH")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovieActors.findAll", query = "SELECT m FROM MovieActors m"),
    @NamedQuery(name = "MovieActors.findById", query = "SELECT m FROM MovieActors m WHERE m.id = :id"),
    @NamedQuery(name = "MovieActors.findByActor1", query = "SELECT m FROM MovieActors m WHERE m.actor1 = :actor1"),
    @NamedQuery(name = "MovieActors.findByActor2", query = "SELECT m FROM MovieActors m WHERE m.actor2 = :actor2")})
public class MovieActors implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ACTOR1")
    private String actor1;
    
    @Size(max = 200)
    @Column(name = "ACTOR2")
    private String actor2;
    
    @JoinColumn(name = "ID", referencedColumnName = "ID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Movie movieEntityGraph;

    public MovieActors() {
    }

    public MovieActors(Integer id) {
        this.id = id;
    }

    public MovieActors(Integer id, String actor1) {
        this.id = id;
        this.actor1 = actor1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActor1() {
        return actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public Movie getMovieEntityGraph() {
        return movieEntityGraph;
    }

    public void setMovieEntityGraph(Movie movieEntityGraph) {
        this.movieEntityGraph = movieEntityGraph;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovieActors)) {
            return false;
        }
        MovieActors other = (MovieActors) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.glassfish.enttygraph.MovieActorsEntityGraph[ id=" + id + " ]";
    }
    
}
