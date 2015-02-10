package org.javaee7.jpa.converter;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Stateless
public class EmployeeRepository {

    @PersistenceContext
    private EntityManager em;

    public void persist(Employee e) {
        em.persist(e);
    }

    public List<Employee> all() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }
}
