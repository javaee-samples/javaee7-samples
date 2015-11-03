package org.javaee7.jpa.listeners;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Kuba Marchwicki
 */

@Stateless
public class RatingService {
    @PersistenceContext
    private EntityManager em;

    public Integer movieRating(String name) {
        return em.createNamedQuery(Rating.FIND_BY_NAME, Rating.class)
            .setParameter("name", name)
            .getSingleResult()
            .getRating();
    }

}
