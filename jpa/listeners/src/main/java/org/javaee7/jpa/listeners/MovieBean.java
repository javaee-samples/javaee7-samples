package org.javaee7.jpa.listeners;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    public void createMovie() {
        Movie m = new Movie(5, "Mission Impossible", "Tom Cruise, Jeremy Renner");
        em.persist(m);
        em.flush();
    }

    public void updateMovie() {
        Movie m = em.createNamedQuery("Movie.findByName", Movie.class)
                    .setParameter("name", "Inception")
                    .getSingleResult();
        m.setName("Inception2");
        em.merge(m);
        em.flush();
    }

    public void deleteMovie() {
        Movie m = em.createNamedQuery("Movie.findByName", Movie.class)
                    .setParameter("name", "Inception2")
                    .getSingleResult();
        em.remove(m);
        em.flush();
    }
}
