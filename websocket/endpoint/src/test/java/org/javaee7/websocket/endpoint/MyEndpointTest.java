package org.javaee7.websocket.endpoint;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyEndpointTest {
    final String TEXT = "Hello World!";

    @ArquillianResource
    URI base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyEndpointText.class,
                MyEndpointTextClient.class,
                MyEndpointByteArray.class,
                MyEndpointByteArrayClient.class,
                MyEndpointByteBuffer.class,
                MyEndpointByteBufferClient.class,
                MyEndpointInputStream.class,
                MyEndpointInputStreamClient.class);
    }

    @Test
    public void testTextEndpoint() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        MyEndpointTextClient.latch = new CountDownLatch(1);
        Session session = connectToServer(MyEndpointTextClient.class, "text");
        assertNotNull(session);
        assertTrue(MyEndpointTextClient.latch.await(2, TimeUnit.SECONDS));
        assertEquals(TEXT, MyEndpointTextClient.response);
    }

    @Test
    public void testEndpointByteBuffer() throws URISyntaxException, DeploymentException, IOException, InterruptedException {
        MyEndpointByteBufferClient.latch = new CountDownLatch(1);
        Session session = connectToServer(MyEndpointByteBufferClient.class, "bytebuffer");
        assertNotNull(session);
        assertTrue(MyEndpointByteBufferClient.latch.await(2, TimeUnit.SECONDS));
        assertArrayEquals(TEXT.getBytes(), MyEndpointByteBufferClient.response);
    }

    @Test
    public void testEndpointByteArray() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
        MyEndpointByteArrayClient.latch = new CountDownLatch(1);
        Session session = connectToServer(MyEndpointByteArrayClient.class, "bytearray");
        assertNotNull(session);
        assertTrue(MyEndpointByteArrayClient.latch.await(2, TimeUnit.SECONDS));
        assertNotNull(MyEndpointByteArrayClient.response);
        assertArrayEquals(TEXT.getBytes(), MyEndpointByteArrayClient.response);
    }

    @Test
    public void testEndpointInputStream() throws DeploymentException, IOException, URISyntaxException, InterruptedException {
        MyEndpointInputStreamClient.latch = new CountDownLatch(1);
        Session session = connectToServer(MyEndpointInputStreamClient.class, "inputstream");
        assertNotNull(session);
        assertTrue(MyEndpointInputStreamClient.latch.await(2, TimeUnit.SECONDS));
        assertNotNull(MyEndpointInputStreamClient.response);
        assertArrayEquals(TEXT.getBytes(), MyEndpointInputStreamClient.response);
    }

    public Session connectToServer(Class<?> endpoint, String uriPart) throws DeploymentException, IOException, URISyntaxException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        URI uri = new URI("ws://"
            + base.getHost()
            + ":"
            + base.getPort()
            + base.getPath()
            + uriPart);
        System.out.println("Connecting to: " + uri);
        return container.connectToServer(endpoint, uri);
    }
}
