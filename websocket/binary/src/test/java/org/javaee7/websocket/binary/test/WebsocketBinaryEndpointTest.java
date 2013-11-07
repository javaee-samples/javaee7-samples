/**
 *
 */
package org.javaee7.websocket.binary.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.ContainerProvider;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.javaee7.websocket.binary.MyEndpointByteArray;
import org.javaee7.websocket.binary.MyEndpointByteBuffer;
import org.javaee7.websocket.binary.MyEndpointClient;
import org.javaee7.websocket.binary.MyEndpointInputStream;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nikos Ballas
 * @author Arun Gupta
 */
//@RunWith(Arquillian.class)
public class WebsocketBinaryEndpointTest {

    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String RESPONSE = "Hello World!";

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file deployable in the jboss instance configuraed in
     * arquillian.xml file.
     */
    @Deployment(testable = false)
//    @TargetsContainer("wildfly-arquillian")
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(MyEndpointByteBuffer.class)
                .addClass(MyEndpointByteArray.class)
                .addClass(MyEndpointInputStream.class)
                .addClass(MyEndpointClient.class)
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
    public void testEndpointByteBuffer() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        Session session = connectToServer("bytebuffer");
        assertNotNull(session);
        System.out.println("Waiting for 2 seconds to receive response");
        Thread.sleep(2000);
        assertNotNull(MyEndpointClient.response);
        assertArrayEquals(RESPONSE.getBytes(), MyEndpointClient.response);
    }

    /**
     * The basic test method for the class {
     *
     * @MyEndpointByteArray }
     *
     * @throws DeploymentException
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testEndpointByteArray() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
        Session session = connectToServer("bytearray");
        assertNotNull(session);
        System.out.println("Waiting for 2 seconds to receive response");
        Thread.sleep(2000);
        assertNotNull(MyEndpointClient.response);
        assertArrayEquals(RESPONSE.getBytes(), MyEndpointClient.response);
    }

    /**
     * The basic test method for the class {
     *
     * @MyEndpointInputStream }
     *
     * @throws DeploymentException
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    public void testEndpointInputStream() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
        Session session = connectToServer("inputstream");
        assertNotNull(session);
        System.out.println("Waiting for 2 seconds to receive response");
        Thread.sleep(2000);
        assertNotNull(MyEndpointClient.response);
        assertArrayEquals(RESPONSE.getBytes(), MyEndpointClient.response);
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
    public Session connectToServer(String endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        return container.connectToServer(MyEndpointClient.class, new URI("ws://localhost:8080/binary/" + endpoint));
    }
}
