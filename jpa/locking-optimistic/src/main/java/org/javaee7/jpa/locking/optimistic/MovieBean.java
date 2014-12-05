package org.javaee7.jpa.locking.optimistic;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Arun Gupta
 */
@Stateless
public class MovieBean {
    @PersistenceContext
    private EntityManager em;

    public List<Movie> listMovies() {
        return em.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }

    public Movie findMovie(Integer id) {
        return em.find(Movie.class, id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Movie readMovie(Integer id) {
        return em.find(Movie.class, id, LockModeType.OPTIMISTIC);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void updateMovie(Integer id, String name) {
        Movie movie = findMovie(id);
        em.lock(movie, LockModeType.OPTIMISTIC);
        movie.setName(name);
        em.merge(movie);
        em.flush();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteMovie(Integer id) {
        em.remove(findMovie(id));
    }
}
