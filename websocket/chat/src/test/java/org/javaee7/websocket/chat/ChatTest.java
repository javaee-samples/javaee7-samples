package org.javaee7.websocket.chat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
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
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class ChatTest {

    @ArquillianResource
    URI base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(ChatEndpoint.class,
                ChatClientEndpoint1.class,
                ChatClientEndpoint2.class);
    }

    @Test
    public void testConnect() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        ChatClientEndpoint1.latch = new CountDownLatch(1);
        final Session session1 = connectToServer(ChatClientEndpoint1.class);
        assertNotNull(session1);
        assertTrue(ChatClientEndpoint1.latch.await(2, TimeUnit.SECONDS));

        assertEquals(ChatClientEndpoint1.TEXT, ChatClientEndpoint1.response);

        ChatClientEndpoint1.latch = new CountDownLatch(1);
        ChatClientEndpoint2.latch = new CountDownLatch(1);
        final Session session2 = connectToServer(ChatClientEndpoint2.class);
        assertNotNull(session2);
        assertTrue(ChatClientEndpoint1.latch.await(2, TimeUnit.SECONDS));
        assertTrue(ChatClientEndpoint2.latch.await(2, TimeUnit.SECONDS));
        assertEquals(ChatClientEndpoint2.TEXT, ChatClientEndpoint1.response);
        assertEquals(ChatClientEndpoint2.TEXT, ChatClientEndpoint2.response);
    }

    public Session connectToServer(Class<?> endpoint) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://"
            + base.getHost()
            + ":"
            + base.getPort()
            + base.getPath()
            + "chat");
        return container.connectToServer(endpoint, uri);
    }
}
