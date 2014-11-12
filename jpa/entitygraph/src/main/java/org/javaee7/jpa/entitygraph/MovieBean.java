package org.javaee7.jpa.entitygraph;

import javax.ejb.Stateless;
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
    private EntityManager em;

    public List<Movie> listMoviesDefault() {
        return em.createNamedQuery("Movie.findAll").getResultList();
    }

    public List<Movie> listMoviesWithActorsFetch() {
        return em.createNamedQuery("Movie.findAll")
                .setHint("javax.persistence.fetchgraph", em.getEntityGraph("movieWithActors"))
                .getResultList();
    }

    public List<Movie> listMoviesWithActorsLoad() {
        return em.createNamedQuery("Movie.findAll")
                .setHint("javax.persistence.loadgraph", em.getEntityGraph("movieWithActors"))
                .getResultList();
    }

    public List<Movie> listMoviesWithActorsAndAwardsFetch() {
        return em.createNamedQuery("Movie.findAll")
                .setHint("javax.persistence.fetchgraph", em.getEntityGraph("movieWithActorsAndAwards"))
                .getResultList();
    }

    public List<Movie> listMoviesWithActorsAndAwardsLoad() {
        return em.createNamedQuery("Movie.findAll")
                .setHint("javax.persistence.loadgraph", em.getEntityGraph("movieWithActorsAndAwards"))
                .getResultList();
    }
}
