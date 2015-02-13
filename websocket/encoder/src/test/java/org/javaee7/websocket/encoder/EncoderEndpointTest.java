package org.javaee7.websocket.encoder;

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
 * @author Nikos Ballas
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class EncoderEndpointTest {

    @ArquillianResource
    URI base;

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyEndpoint.class,
                MyMessage.class,
                MyMessageEncoder.class,
                MyMessageDecoder.class);
    }

    @Test
    public void testEndpointEmptyJSONArray() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        final Session session = connectToServer(MyEndpointClientEmptyJSONArray.class);
        assertNotNull(session);
        assertTrue(MyEndpointClientEmptyJSONArray.latch.await(2, TimeUnit.SECONDS));
        assertEquals("{}", MyEndpointClientEmptyJSONArray.response);
    }

    @Test
    public void testEndpointEmptyJSONObject() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        String JSON = "{\"apple\":\"red\",\"banana\":\"yellow\"}";
        Session session = connectToServer(MyEndpointClientJSONObject.class);
        assertNotNull(session);
        assertTrue(MyEndpointClientJSONObject.latch.await(2, TimeUnit.SECONDS));
        assertEquals(JSON, MyEndpointClientJSONObject.response);
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
    public Session connectToServer(Class<?> endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://"
            + base.getHost()
            + ":"
            + base.getPort()
            + base.getPath()
            + "encoder");
        return container.connectToServer(endpoint, uri);
    }
}
