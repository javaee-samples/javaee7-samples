package org.javaee7.jpa.pu.typesafe;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * @author Arun Gupta
 */
@Stateless
public class MySessionBean {
    
    @Inject @DefaultDatabase
    EntityManager defaultEM;

//    @PersistenceContext(unitName = "defaultPU")
//    EntityManager defaultEM;
//
    public List<Movie> listMovies() {
        return defaultEM.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }
}
