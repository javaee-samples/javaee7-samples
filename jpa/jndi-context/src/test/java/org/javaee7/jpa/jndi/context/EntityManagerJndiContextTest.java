package org.javaee7.jpa.jndi.context;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class EntityManagerJndiContextTest {
    @Inject
    private EmployeeBean employeeBean;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.jndi.context")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testEntityManagerLookup() throws Exception {
        InitialContext context = new InitialContext();
        EntityManager entityManager = (EntityManager) context.lookup("java:comp/env/persistence/myJNDI");
        assertNotNull(entityManager);

        List<Employee> employees = entityManager.createNamedQuery("Employee.findAll", Employee.class).getResultList();
        assertFalse(employees.isEmpty());
        assertEquals(8, employees.size());
    }

    @Test
    public void testEntityManagerLookupBean() throws Exception {
        List<Employee> employees = employeeBean.get();
        assertFalse(employees.isEmpty());
        assertEquals(8, employees.size());
    }
}
