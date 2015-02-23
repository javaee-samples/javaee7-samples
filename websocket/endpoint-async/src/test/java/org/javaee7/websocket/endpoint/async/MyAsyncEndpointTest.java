package org.javaee7.websocket.endpoint.async;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.websocket.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;

import static com.jayway.awaitility.Awaitility.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 *
 * @author Jacek Jackowiak
 */
@RunWith(Arquillian.class)
public class MyAsyncEndpointTest {

    private static final String TEST_MESSAGE = "test";

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive deploy() throws URISyntaxException {
        return ShrinkWrap.create(WebArchive.class)
            .addClass(MyAsyncEndpointText.class)
            .addClass(MyAsyncEndpointByteBuffer.class);
    }

    @Test
    public void shouldReceiveAsyncTextMessage() throws URISyntaxException, IOException, DeploymentException {
        MyAsyncEndpointTextClient endpoint = new MyAsyncEndpointTextClient();
        Session session = connectToEndpoint(endpoint, "text");

        session.getAsyncRemote().sendText(TEST_MESSAGE);

        await().untilCall(to(endpoint).getReceivedMessage(), is(equalTo(TEST_MESSAGE)));
    }

    @Test
    public void shouldReceiveAsyncByteBufferMessage() throws URISyntaxException, IOException, DeploymentException {
        final ByteBuffer buffer = ByteBuffer.wrap(TEST_MESSAGE.getBytes());
        MyAsyncEndpointByteBufferClient endpoint = new MyAsyncEndpointByteBufferClient();
        Session session = connectToEndpoint(endpoint, "bytebuffer");

        session.getAsyncRemote().sendBinary(buffer);

        await().untilCall(to(endpoint).getReceivedMessage(), is(notNullValue()));
        String receivedString = bufferToString(endpoint.getReceivedMessage());
        assertThat(receivedString, is(equalTo(TEST_MESSAGE)));
    }

    private String bufferToString(ByteBuffer buffer) throws UnsupportedEncodingException {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.duplicate().get(bytes);
        return new String(bytes, "UTF-8");
    }

    private Session connectToEndpoint(Object endpoint, String uriPart) throws URISyntaxException, DeploymentException, IOException {
        URI uri = getURI(uriPart);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        return container.connectToServer(endpoint, uri);
    }

    private URI getURI(String uriPart) throws URISyntaxException {
        return new URI("ws://"
            + base.getHost()
            + ":"
            + base.getPort()
            + base.getPath()
            + uriPart);
    }
}
