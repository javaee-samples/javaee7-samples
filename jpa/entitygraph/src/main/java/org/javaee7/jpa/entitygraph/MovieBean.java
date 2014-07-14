package org.javaee7.jpa.entitygraph;

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
    EntityManager em;

    public List<Movie> listMovies() {
        return em.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }    
}
