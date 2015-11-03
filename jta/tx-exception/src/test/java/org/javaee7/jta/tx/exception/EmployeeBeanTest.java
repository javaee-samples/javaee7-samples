package org.javaee7.jta.tx.exception;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * This test is RED with WildFly 8.0.0.Beta1 because it does not have a standard default DataSource.
 *
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class EmployeeBeanTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(EmployeeBean.class, Employee.class)
            .addAsManifestResource("beans.xml")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/load.sql")
            .addAsResource("META-INF/drop.sql");
    }

    @Inject
    EmployeeBean bean;

    @Test
    public void should_have_7_employees() {
        assertEquals(7, bean.getEmployees().size());
    }

    @Test
    public void should_have_1_more_employee_after_checked_exception() {
        try {
            bean.addAndThrowChecked();
        } catch (Exception ex) {
        }
        assertEquals(8, bean.getEmployees().size());
    }

    @Test
    public void should_not_have_1_more_employee_after_runtime_exception() {
        try {
            bean.addAndThrowRuntime();
        } catch (Exception ex) {
        }
        assertEquals(7, bean.getEmployees().size());
    }

}
