package org.javaee7.jms.temp.destination;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

/**
 * Temporary queues are JMS queues that exist for the lifetime of single JMS connection.
 * Also the reception of the messages is exclusive to the connection, therefore no
 * reasonable use case exist for temporary topic within Java EE container, as connection
 * is exclusive to single component.
 *
 * Temporary queues are usually used as reply channels for request / response communication
 * over JMS.
 */
@RunWith(Arquillian.class)
public class TempQueueTest {

    /**
     * In this test we created a server component +RequestResponseOverJMS+, that
     * listens on a Queue and passes the response to the destination specified in
     * +JMSReplyTo+ header of the message.
     *
     * include::RequestResponseOverJMS#onMessage[]
     *
     * +JmsClient+ is a client to this server, and has to be non transactional,
     * otherwise the request would be first sent upon commit, i. e. after the
     * business method finishes. That would be too late. We need to send the message
     * immediately, and wait for the response to arrive.
     *
     * include::JmsClient#process[]
     *
     */
    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(RequestResponseOverJMS.class, JmsClient.class, Resources.class);
    }

    @EJB
    JmsClient client;

    /**
     * We invoke the client, and verify that the response is processed
     */
    @Test
    public void testRequestResposne() {
        String response = client.process("Hello");
        Assert.assertEquals("Processed: Hello", response);
    }
}
