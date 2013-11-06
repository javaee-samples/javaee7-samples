/**
 * 
 */
package org.javaee7.jaxrs.endpoint.test;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;

import junit.framework.Assert;

import org.javaee7.jaxrs.endpoint.Database;
import org.javaee7.jaxrs.endpoint.MyApplication;
import org.javaee7.jaxrs.endpoint.MyResource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nikolaos Ballas
 *
 */
@RunWith(Arquillian.class)
public class JaxRSEndpointTest {
	
	private Client client = null;
	
	@Deployment(testable=false) @TargetsContainer("wildfly-arquillian")
	public static WebArchive createDeplymentI() {
		return ShrinkWrap.create(WebArchive.class, "jaxrs-endpoint.war").
				addClass(MyApplication.class).
				addClass(Database.class).
				addClass(MyResource.class);
	}
	
	
	@Test
	@InSequence(1)
	public void invokeEndpointPUTTest() throws URISyntaxException{
		WebTarget targetEndpoint= getTargetEndpoint("fruit");
		targetEndpoint.request().put(Entity.text("banana"));
	}
	@Test
	@InSequence(2)
	public void invokeEndpointGETTest() throws URISyntaxException {
		WebTarget targetEndPoint = getTargetEndpoint("fruit");
		String received = targetEndPoint.request().get(String.class);
		Assert.assertEquals("[banana]" ,received);
	}
	
	/**
	 * The method for obtaining a target endpoint.
	 * @param endpointName the target endpoint name which we want to invoke
	 * @return a WebTarget object
	 * @throws URISyntaxException
	 */
	public WebTarget getTargetEndpoint(String endpointName) throws URISyntaxException  {
		if(client == null) {
			client = ClientBuilder.newClient();
		}
		return client.target(new URI("http://localhost:8080/jaxrs-endpoint/webresources/"+endpointName));
	}
}
