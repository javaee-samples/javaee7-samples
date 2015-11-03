package org.javaee7.jpa.multiple.pu;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Arun Gupta
 */
@Stateless
public class MultiplePuBean {
    @PersistenceContext(unitName = "defaultPU")
    private EntityManager defaultEM;
    @PersistenceContext(unitName = "samplePU")
    private EntityManager sampleEM;

    public List<Movie> listMovies() {
        return defaultEM.createNamedQuery("Movie.findAll", Movie.class).getResultList();
    }

    public List<ProductCode> listProductCode() {
        return sampleEM.createNamedQuery("ProductCode.findAll", ProductCode.class).getResultList();
    }
}
