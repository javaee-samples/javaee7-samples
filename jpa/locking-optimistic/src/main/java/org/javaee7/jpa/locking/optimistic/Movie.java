package org.javaee7.jpa.locking.optimistic;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "MOVIE_OPTIMISTIC")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.findById", query = "SELECT m FROM Movie m WHERE m.id = :id"),
    @NamedQuery(name = "Movie.findByName", query = "SELECT m FROM Movie m WHERE m.name = :name"),
    @NamedQuery(name = "Movie.findByActors", query = "SELECT m FROM Movie m WHERE m.actors = :actors")})
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    private Integer id;

    @Version int version;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 200)
    private String actors;
    
    public Movie() {
    }

    public Movie(Integer id) {
        this.id = id;
    }

    public Movie(Integer id, String name, String actors) {
        this.id = id;
        this.name = name;
        this.actors = actors;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
