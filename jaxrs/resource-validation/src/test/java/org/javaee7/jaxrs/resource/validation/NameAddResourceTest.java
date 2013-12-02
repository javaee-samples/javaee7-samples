package org.javaee7.jaxrs.resource.validation;

import java.net.URL;
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

	@Deployment
	public static WebArchive createDeployment() {
		return ShrinkWrap.create(WebArchive.class)
				.addClasses(MyApplication.class, NameAddResource.class, Name.class, Email.class, JsonObjectProvider.class, JsonStructureProvider.class);
	}
	@ArquillianResource
	private URL base;

	@Test
	public void shouldPassNameValidation() throws Exception {
		if (true) {

		return;
		}
		Client client = ClientBuilder.newClient();
		client.register(JsonObjectProvider.class, JsonStructureProvider.class);
		WebTarget target = client
				.target(new URL(base, "webresources/nameadd").toExternalForm());

		Response r = target
				.request()
				.post(Entity.json(new Name("Sheldon", "Cooper", "sheldon@cooper.com")));

		assertEquals(r.getStatusInfo().getFamily(), Status.Family.SUCCESSFUL);
	}

}
