package org.javaee7.jaxrs.resource.validation;

import static javax.ws.rs.client.Entity.xml;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;

import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class NameAddResourceTest {

    @ArquillianResource
    private URL base;
    
    private WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyApplication.class, 
                NameAddResource.class, 
                Name.class, 
                Email.class, 
                EmailValidator.class);
    }

    @Before
    public void setUp() throws Exception {
        target = ClientBuilder.newClient()
                              .target(new URL(base, MyApplication.PATH + NameAddResource.PATH)
                              .toURI());
    }

    @Test
    public void shouldPassNameValidation() throws Exception {
        assertStatus(postName(startValidName()), OK);
    }

    @Test
    public void shouldFailAtFirstNameSizeValidation() throws Exception {
        Name name = startValidName();
        name.setFirstName("");

        assertFailedValidation(postName(name));
    }

    @Test
    public void shouldFailAtFirstNameNullValidation() throws Exception {
        Name name = startValidName();
        name.setFirstName(null);

        assertFailedValidation(postName(name));
    }

    @Test
    public void shouldFailAtLastNameSizeValidation() throws Exception {
        Name name = startValidName();
        name.setLastName("");

        assertFailedValidation(postName(name));
    }

    @Test
    public void shouldFailAtLastNameNullValidation() throws Exception {
        Name name = startValidName();
        name.setLastName(null);

        assertFailedValidation(postName(name));
    }

    @Test
    public void shouldFailAtEmailAtSymbolValidation() throws Exception {
        Name name = startValidName();
        name.setEmail("missing-at-symbol.com");

        assertFailedValidation(postName(name));
    }

    @Test
    public void shouldFailAtEmailComDomainValidation() throws Exception {
        Name name = startValidName();
        name.setEmail("other-than-com@domain.pl");

        assertFailedValidation(postName(name));
    }
    
    private Name startValidName() {
        return new Name(
            "Sheldon", 
            "Cooper", 
            "random@example.com");
    }

    private Response postName(Name name) {
        return target
            .request()
            .post(xml(name));
    }

    private void assertStatus(Response response, Status expectedStatus) {
        assertEquals(response.getStatusInfo(), expectedStatus);
    }
    
    private void assertFailedValidation(Response response) {
        assertStatus(response, BAD_REQUEST);
    }

}
