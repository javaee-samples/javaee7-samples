/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.websocket.encoder.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
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
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyClientTest {
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
    public void testEndpointEmptyJSONObject() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        final String JSON = "{\"apple\" : \"red\", \"banana\": \"yellow\"}";
        Session session = connectToServer(MyClient.class);
        assertNotNull(session);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String text) {
                assertEquals(JSON, text);
            }
        });
        assertTrue(MyClient.latch.await(2, TimeUnit.SECONDS));
    }
    
    public Session connectToServer(Class endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        assertNotNull(container);
        assertNotNull(base);
        URI uri = new URI("ws://"
                + base.getHost()
                + ":"
                + base.getPort()
                + "/"
                + base.getPath()
                + "/encoder-client");
        return container.connectToServer(endpoint, uri);
    }
    
}
