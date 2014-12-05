package org.javaee7.jpasamples.schema.gen.scripts.external;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @author Arun Gupta
 */
@Stateless
public class EmployeeBean {
    @PersistenceContext
    private EntityManager em;

    public List<Employee> get() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }
}
