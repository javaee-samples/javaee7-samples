package org.javaee7.jpa.multiple.pu;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Stateless
public class MySessionBean {

    @PersistenceContext(unitName = "defaultPU")
    EntityManager defaultEM;

    @PersistenceContext(unitName = "samplePU")
    EntityManager sampleEM;

    public List<Movie> listMovies() {
        return defaultEM.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }
    
    public List<ProductCode> listProductCode() {
        return sampleEM.createNamedQuery("ProductCode.findAll", ProductCode.class).getResultList();
    }

}
