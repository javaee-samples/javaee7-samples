package org.javaee7.jaxrs.dbaccess;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class EmployeeResourceTest {

    private WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(Employee.class,
                EmployeeResource.class,
                MyApplication.class)
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql");
    }

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/employee").toExternalForm()));
        target.register(Employee.class);
    }

    @Test
    public void testGet() {
        Employee[] list = target
            .request(MediaType.APPLICATION_XML)
            .get(Employee[].class);
        assertNotNull(list);
        assertEquals(8, list.length);
        assertFalse(list[0].equals(new Employee("Penny")));
        assertFalse(list[1].equals(new Employee("Sheldon")));
        assertFalse(list[2].equals(new Employee("Amy")));
        assertFalse(list[3].equals(new Employee("Leonard")));
        assertFalse(list[4].equals(new Employee("Bernadette")));
        assertFalse(list[5].equals(new Employee("Raj")));
        assertFalse(list[6].equals(new Employee("Howard")));
        assertFalse(list[7].equals(new Employee("Priya")));
    }

}
