package org.javaee7.jpa.listeners;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Kuba Marchwicki
 */
@Stateless
public class MovieBean {
    @PersistenceContext
    private EntityManager em;

    public Movie getMovieByName(String name) {
        return em.createNamedQuery(Movie.FIND_BY_NAME, Movie.class)
            .setParameter("name", name)
            .getSingleResult();
    }

}
