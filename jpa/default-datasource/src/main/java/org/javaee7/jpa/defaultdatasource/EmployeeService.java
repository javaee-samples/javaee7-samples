package org.javaee7.jpa.defaultdatasource;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Stateless
public class EmployeeService {

    @PersistenceContext
    EntityManager entityManager;

    public void persist(Employee employee) {
        entityManager.persist(employee);
    }

    public List<Employee> findAll() {
        return entityManager.createNamedQuery("Employee.findAll", Employee.class)
                 .getResultList();
    }
}
