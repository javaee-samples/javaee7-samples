package org.javaee7.jpa.defaultdatasource;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Stateless
public class EmployeeBean {

    @PersistenceContext
    EntityManager em;

    public void persist(Employee e) {
        em.persist(e);
    }

    public List<Employee> get() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }
}
