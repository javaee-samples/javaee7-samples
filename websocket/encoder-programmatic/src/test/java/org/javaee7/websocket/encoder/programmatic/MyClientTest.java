package org.javaee7.websocket.encoder.programmatic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyClientTest {
    @ArquillianResource
    URI base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyEndpoint.class,
                MyEndpointConfiguration.class,
                MyMessage.class,
                MyMessageEncoder.class,
                MyMessageDecoder.class);
    }

    @Test
    public void testEndpoint() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        final String JSON = "{\"apple\":\"red\",\"banana\":\"yellow\"}";
        Session session = connectToServer(MyClient.class);
        assertNotNull(session);
        assertTrue(MyClient.latch.await(2, TimeUnit.SECONDS));
        assertEquals(JSON, MyClient.response.toString());
    }

    public Session connectToServer(Class<?> endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        assertNotNull(container);
        assertNotNull(base);
        URI uri = new URI("ws://"
            + base.getHost()
            + ":"
            + base.getPort()
            + base.getPath()
            + "encoder-programmatic");
        return container.connectToServer(endpoint, uri);
    }

}
