package org.javaee7.jpa.criteria;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

/**
 * @author Arun Gupta
 */
@Stateless
public class MovieBean {
    @PersistenceContext
    private EntityManager em;

    public List<Movie> listMovies() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Movie> listCriteria = builder.createQuery(Movie.class);
        Root<Movie> listRoot = listCriteria.from(Movie.class);
        listCriteria.select(listRoot);
        TypedQuery<Movie> query = em.createQuery(listCriteria);
        return query.getResultList();
    }

    public void updateMovie() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaUpdate<Movie> updateCriteria = builder.createCriteriaUpdate(Movie.class);
        Root<Movie> updateRoot = updateCriteria.from(Movie.class);
        updateCriteria.where(builder.equal(updateRoot.get(Movie_.name), "Inception"));
        updateCriteria.set(updateRoot.get(Movie_.name), "INCEPTION");
        Query q = em.createQuery(updateCriteria);
        q.executeUpdate();
        em.flush();
    }

    public void deleteMovie() {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaDelete<Movie> deleteCriteria = builder.createCriteriaDelete(Movie.class);
        Root<Movie> updateRoot = deleteCriteria.from(Movie.class);
        deleteCriteria.where(builder.equal(updateRoot.get(Movie_.name), "The Matrix"));
        Query q = em.createQuery(deleteCriteria);
        q.executeUpdate();
        em.flush();
    }
}
