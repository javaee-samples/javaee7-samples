package org.javaee7.jpa.listeners;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Kuba Marchwicki
 */
@Entity
@Table(name = "MOVIE_RATINGS")
@NamedQueries({
    @NamedQuery(name = Rating.FIND_BY_NAME, query = "SELECT r FROM Rating r WHERE r.name = :name")
})
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String FIND_BY_NAME = "Rating.findByName";

    @Id
    @NotNull
    private Integer id;

    @NotNull
    @Size(min = 1, max = 50)
    private String name;

    @NotNull
    private Integer rating;

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Rating))
            return false;

        Rating ratings = (Rating) o;

        return id.equals(ratings.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
