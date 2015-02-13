package org.javaee7.jpa.schemagen.metadata;

import org.javaee7.jpa.schemagen.metadata.Employee;
import org.javaee7.jpa.schemagen.metadata.EmployeeBean;
import java.util.List;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class EmployeeBeanTest {

    @Inject
    EmployeeBean bean;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(Employee.class,
                EmployeeBean.class)
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql");
    }

    @Test
    public void testGet() throws Exception {
        assertNotNull(bean);
        List<Employee> list = bean.get();
        assertNotNull(list);
        assertEquals(8, list.size());
        assertFalse(list.contains(new Employee("Penny")));
        assertFalse(list.contains(new Employee("Sheldon")));
        assertFalse(list.contains(new Employee("Amy")));
        assertFalse(list.contains(new Employee("Leonard")));
        assertFalse(list.contains(new Employee("Bernadette")));
        assertFalse(list.contains(new Employee("Raj")));
        assertFalse(list.contains(new Employee("Howard")));
        assertFalse(list.contains(new Employee("Priya")));
    }

}
