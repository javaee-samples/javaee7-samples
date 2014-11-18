package org.javaee7.jpa.entitygraph;

import javax.ejb.Stateless;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Arun Gupta
 */
@SuppressWarnings("unchecked")
@Stateless
public class MovieBean {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Movie> listMovies() {
        return entityManager.createNamedQuery("Movie.findAll")
                            .getResultList();
    }

    public List<Movie> listMovies(String hint, String graphName) {
        return entityManager.createNamedQuery("Movie.findAll")
                            .setHint(hint, entityManager.getEntityGraph(graphName))
                            .getResultList();
    }

    public List<Movie> listMovies(String hint, EntityGraph<?> entityGraph) {
        return entityManager.createNamedQuery("Movie.findAll")
                            .setHint(hint, entityGraph)
                            .getResultList();
    }
}
