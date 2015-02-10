package org.javaee7.jaxrs.resource.validation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;
import java.net.URL;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class NameAddResourceTest {

    @ArquillianResource
    private URL base;
    private WebTarget target;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyApplication.class, NameAddResource.class, Name.class, Email.class, EmailValidator.class);
    }

    @Before
    public void setUp() throws Exception {
        Client client = ClientBuilder.newClient();
        String resourcePath = MyApplication.PATH + NameAddResource.PATH;
        URI resourceUri = new URL(base, resourcePath).toURI();
        target = client.target(resourceUri);
    }

    @Test
    public void shouldPassNameValidation() throws Exception {
        JsonObject name = startValidName()
            .build();

        Response response = postName(name);

        assertStatus(response, OK);
    }

    private JsonObjectBuilder startValidName() {
        return Json.createObjectBuilder()
            .add("firstName", "Sheldon")
            .add("lastName", "Cooper")
            .add("email", "random@example.com");
    }

    private Response postName(JsonObject name) {
        Entity<JsonObject> nameEntity = Entity.json(name);
        return target
            .request()
            .post(nameEntity);
    }

    private void assertStatus(Response response, Status expectedStatus) {
        Response.StatusType actualStatus = response.getStatusInfo();
        assertEquals(actualStatus, expectedStatus);
    }

    @Test
    public void shouldFailAtFirstNameSizeValidation() throws Exception {
        JsonObject name = startValidName()
            .add("firstName", "")
            .build();

        Response response = postName(name);

        assertFailedValidation(response);
    }

    private void assertFailedValidation(Response response) {
        assertStatus(response, BAD_REQUEST);
    }

    @Test
    public void shouldFailAtFirstNameNullValidation() throws Exception {
        JsonObject name = startValidName()
            .addNull("firstName")
            .build();

        Response response = postName(name);

        assertFailedValidation(response);
    }

    @Test
    public void shouldFailAtLastNameSizeValidation() throws Exception {
        JsonObject name = startValidName()
            .add("lastName", "")
            .build();

        Response response = postName(name);

        assertFailedValidation(response);
    }

    @Test
    public void shouldFailAtLastNameNullValidation() throws Exception {
        JsonObject name = startValidName()
            .addNull("lastName")
            .build();

        Response response = postName(name);

        assertFailedValidation(response);
    }

    @Test
    public void shouldFailAtEmailAtSymbolValidation() throws Exception {
        JsonObject name = startValidName()
            .add("email", "missing-at-symbol.com")
            .build();

        Response response = postName(name);

        assertFailedValidation(response);
    }

    @Test
    public void shouldFailAtEmailComDomainValidation() throws Exception {
        JsonObject name = startValidName()
            .add("email", "other-than-com@domain.pl")
            .build();

        Response response = postName(name);

        assertFailedValidation(response);
    }

}
