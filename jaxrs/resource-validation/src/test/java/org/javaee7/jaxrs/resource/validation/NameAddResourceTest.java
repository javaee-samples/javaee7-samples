package org.javaee7.jaxrs.resource.validation;

import java.net.URL;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.plugins.providers.jsonp.JsonObjectProvider;
import org.jboss.resteasy.plugins.providers.jsonp.JsonStructureProvider;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class NameAddResourceTest {

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addClasses(MyApplication.class, NameAddResource.class, Name.class, Email.class, EmailValidator.class);
	}
	@ArquillianResource
	private URL base;

	@Test
	public void shouldPassNameValidation() throws Exception {
		Client client = ClientBuilder.newClient();
		client.register(JsonObjectProvider.class, JsonStructureProvider.class);
		WebTarget target = client
				.target(new URL(base, "webresources/nameadd").toExternalForm());
		JsonObject name = Json.createObjectBuilder()
				.add("firstName", "Sheldon")
				.add("lastName", "Cooper")
				.add("email", "random@example.com")
				.build();

		Response r = target
				.request()
				.post(Entity.json(name));

		assertEquals(r.getStatusInfo().getFamily(), Status.Family.SUCCESSFUL);
	}
	@Test
	public void shouldFailAtFirstNameValidation() throws Exception {
		Client client = ClientBuilder.newClient();
		client.register(JsonObjectProvider.class, JsonStructureProvider.class);
		WebTarget target = client
				.target(new URL(base, "webresources/nameadd").toExternalForm());
		JsonObject name = Json.createObjectBuilder()
				.add("firstName", "")
				.add("lastName", "Cooper")
				.add("email", "random@example.com")
				.build();

		Response r = target
				.request()
				.post(Entity.json(name));

		assertEquals(r.getStatusInfo().getFamily(), Status.Family.CLIENT_ERROR);
	}

}
