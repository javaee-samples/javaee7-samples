package org.javaee7.jpa.nativesql.resultset.mapping;

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
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Employee> get() {
        return em.createNativeQuery("select * from EMPLOYEE_NATIVE_SQL_RESULTSET_MAPPING", "myMapping").getResultList();
    }
}
