package org.javaee7.jta.tx.exception;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * @author Arun Gupta
 */
public class EmployeeBean {
    @PersistenceContext
    EntityManager em;

    @Transactional
    public void addAndThrowChecked() throws Exception {
        em.persist(new Employee(8, "Priya"));
        throw new Exception();
    }

    @Transactional
    public void addAndThrowRuntime() {
        em.persist(new Employee(9, "Priya"));
        throw new RuntimeException();
    }

    public List<Employee> getEmployees() {
        System.out.println("getEmployees");
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }
}
