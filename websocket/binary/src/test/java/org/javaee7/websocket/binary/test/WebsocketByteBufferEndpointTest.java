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
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Nikos "cirix" Ballas
 *
 */
@RunWith(Arquillian.class)
public class WebsocketByteBufferEndpointTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
    @Deployment(testable = false)
    @TargetsContainer("wildfly-arquillian")
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(MyEndpointByteBuffer.class).
                addClass(MyEndpointByteArray.class).
                addClass(MyEndpointInputStream.class).
                addAsWebResource(new File(WEBAPP_SRC, "index.jsp")).
                addAsWebResource(new File(WEBAPP_SRC, "websocket.js"));
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
    public void testEndPointByteBuffer() throws URISyntaxException, DeploymentException, IOException {
        WebSocketContainer wSocketContainer = ContainerProvider.getWebSocketContainer();
        wSocketContainer.connectToServer(MyEndpointClient.class, new URI("ws://localhost:8080/binary/bytearray"));
    }
}
