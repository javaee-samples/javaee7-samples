package org.javaee7.jpa.defaultdatasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.runners.MethodSorters.NAME_ASCENDING;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
@FixMethodOrder(NAME_ASCENDING)
public class EmployeeServiceTest {

    @Inject
    EmployeeService employeeService;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                Employee.class,
                EmployeeService.class)
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql");
    }

    @Test
    public void T1_testGet() throws Exception {
        assertNotNull(employeeService);

        List<Employee> employees = employeeService.findAll();

        assertNotNull(employees);
        assertEquals(8, employees.size());

        assertFalse(employees.contains(new Employee("Penny")));
        assertFalse(employees.contains(new Employee("Sheldon")));
        assertFalse(employees.contains(new Employee("Amy")));
        assertFalse(employees.contains(new Employee("Leonard")));
        assertFalse(employees.contains(new Employee("Bernadette")));
        assertFalse(employees.contains(new Employee("Raj")));
        assertFalse(employees.contains(new Employee("Howard")));
        assertFalse(employees.contains(new Employee("Priya")));
    }

    @Test
    public void T2_testPersist() throws Exception {

        Employee newEmployee = new Employee("Reza");

        employeeService.persist(newEmployee);

        List<Employee> employees = employeeService.findAll();
        assertNotNull(employees);
        assertEquals(9, employees.size());

        boolean rezaInList = false;
        for (Employee employee : employees) {
            if (employee.getName().equals("Reza")) {
                rezaInList = true;
                break;
            }
        }

        assertTrue(rezaInList);
    }

}
