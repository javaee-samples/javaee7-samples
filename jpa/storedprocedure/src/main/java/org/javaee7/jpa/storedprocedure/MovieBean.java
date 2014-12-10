package org.javaee7.jpa.storedprocedure;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Stateless
public class MovieBean {
    @PersistenceContext
    private EntityManager em;

    public List<Movie> listMovies() {
        return em.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }

    public void executeStoredProcedure() {
        em.createNamedStoredProcedureQuery("top10Movies").execute();
    }
}
