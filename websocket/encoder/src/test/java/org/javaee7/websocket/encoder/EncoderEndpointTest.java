package org.javaee7.websocket.encoder;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
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
                        MyMessageDecoder.class,
                        MyEndpointClientEmptyJSONArray.class,
                        MyEndpointClientJSONObject.class);
    }

    @Test
    public void testEndpointEmptyJSONArray() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        final Session session = connectToServer(MyEndpointClientEmptyJSONArray.class);
        assertNotNull(session);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String text) {
                assertEquals("{}", text);
            }
        });
        assertTrue(MyEndpointClientEmptyJSONArray.latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    public void testEndpointEmptyJSONObject() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        final String JSON = "{\"apple\" : \"red\", \"banana\": \"yellow\"}";
        Session session = connectToServer(MyEndpointClientJSONObject.class);
        assertNotNull(session);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String text) {
                assertEquals(JSON, text);
            }
        });
        assertTrue(MyEndpointClientJSONObject.latch.await(2, TimeUnit.SECONDS));
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
    public Session connectToServer(Class endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://"
                + base.getHost()
                + ":"
                + base.getPort()
                + "/"
                + base.getPath()
                + "/encoder");
        return container.connectToServer(endpoint, uri);

    }
}
