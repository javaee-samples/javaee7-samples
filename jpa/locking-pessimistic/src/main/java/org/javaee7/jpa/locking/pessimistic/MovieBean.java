package org.javaee7.jpa.locking.pessimistic;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Stateless
public class MovieBean {

    @PersistenceContext
    EntityManager em;

    public List<Movie> listMovies() {
        return em.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }

    public void updateMovie() {
        Movie m = em.find(Movie.class, 3, LockModeType.PESSIMISTIC_WRITE);
        em.lock(m, LockModeType.PESSIMISTIC_WRITE);
        m.setName("INCEPTION");
        em.merge(m);
        em.flush();
    }

    public void deleteMovie() {
        Movie m = em.find(Movie.class, 1, LockModeType.PESSIMISTIC_WRITE);
        em.remove(m);
        em.flush();
    }

}
