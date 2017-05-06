package org.javaee7.jpa.converter;

import static java.util.Arrays.asList;
import static org.jboss.shrinkwrap.api.ArchivePaths.create;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class EmployeeRepositoryTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = create(WebArchive.class)
                .addPackage("org.javaee7.jpa.converter")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/load.sql")
                .addAsWebInfResource(INSTANCE, create("beans.xml"));
            
            System.out.println(war.toString(true));
            
            return war;
    }

    @Inject
    private EmployeeRepository repository;

    @Test
    public void should_return_all_employee_records() throws Exception {
        
        // When
        final List<Employee> actualEmployees = repository.all();

        // Then
        assertTrue(actualEmployees.size() == 6);
        
        List<Employee> expectedEmployees = asList(
            employee("Leonard", "11-22-33-44"), employee("Sheldon", "22-33-44-55"),
            employee("Penny", "33-44-55-66"), employee("Raj", "44-55-66-77"),
            employee("Howard", "55-66-77-88"), employee("Bernadette", "66-77-88-99"));
        
        for (Employee employee : expectedEmployees) {
            assertTrue(actualEmployees.contains(employee));
        }
    }

    // -- Test utility method

    private static Employee employee(String name, String creditCardNumber) {
        return new Employee(name, new CreditCard(creditCardNumber));
    }
   
}
