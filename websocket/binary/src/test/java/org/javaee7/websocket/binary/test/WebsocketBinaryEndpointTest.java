/**
 * 
 */
package org.javaee7.websocket.binary.test;

import io.undertow.websockets.jsr.UndertowContainerProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import junit.framework.Assert;

import org.javaee7.websocket.binary.MyEndpointByteArray;
import org.javaee7.websocket.binary.MyEndpointByteBuffer;
import org.javaee7.websocket.binary.MyEndpointClient;
import org.javaee7.websocket.binary.MyEndpointInputStream;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nikos  Ballas
 * 
 */
@RunWith(Arquillian.class)
public class WebsocketBinaryEndpointTest {
	private static final String WEBAPP_SRC = "src/main/webapp";

	/**
	 * Arquillian specific method for creating a file which can be deployed
	 * while executing the test.
	 * 
	 * @return a war file deployable in the jboss instance configuraed in arquillian.xml file.
	 */
	@Deployment(testable = false)
	@TargetsContainer("wildfly-arquillian")
	public static WebArchive createDeployment() {
		WebArchive war = ShrinkWrap.create(WebArchive.class)
				.addClass(MyEndpointByteBuffer.class)
				.addClass(MyEndpointByteArray.class)
				.addClass(MyEndpointInputStream.class)
				.addClass(MyEndpointClient.class)
				.addAsWebResource(new File(WEBAPP_SRC, "index.jsp"))
				.addAsWebResource(new File(WEBAPP_SRC, "websocket.js"));
		return war;
	}

	/**
	 * The basic test method for the class {@link MyEndpointByteBuffer}
	 * 
	 * @throws URISyntaxException
	 * @throws DeploymentException
	 * @throws IOException
	 */
	@Test
	public void testEndpointByteBuffer() throws URISyntaxException,DeploymentException, IOException {
		Session session = connectToServer("bytebuffer");
		Assert.assertNull(session);
	}

	/**
	 * The basic test method for the class {@MyEndpointByteArray
	 * }
	 * 
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testEndpointByteArray() throws DeploymentException,IOException, URISyntaxException {
		Session session = connectToServer("bytearray");
		Assert.assertNull(session);
	}

	/**
	 * The basic test method for the class {@MyEndpointInputStream
	 * }
	 * 
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@Test
	public void testEndpointInputStream() throws DeploymentException,IOException, URISyntaxException {
		Session session = connectToServer("inputstream");
		Assert.assertNull(session);
	}

	/**
	 * Method used to supply connection to the server by passing the naming of
	 * the websocket endpoint
	 * 
	 * @param endpoint
	 * @return
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Session connectToServer(String endpoint) throws DeploymentException,	IOException, URISyntaxException {
		WebSocketContainer wSocketContainer = UndertowContainerProvider.getWebSocketContainer();
		return wSocketContainer.connectToServer(MyEndpointClient.class,	new URI("ws://localhost:8080/binary/" + endpoint));
	}
}
