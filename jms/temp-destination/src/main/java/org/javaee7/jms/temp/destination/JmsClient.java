package org.javaee7.jms.temp.destination;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.*;
import java.lang.IllegalStateException;

/**
 * Client receiving response to a message via temporary queue.
 * The client has to be non-trasactional, as we need to send message in the middle
 * of the method.
 * @author Patrik Dudits
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class JmsClient {

    @Resource(name = Resources.REQUEST_QUEUE)
    Queue requestQueue;

    @Inject
    JMSContext jms;

    public String process(String request) {

        // prepare the request message
        TextMessage requestMessage = jms.createTextMessage(request);
        TemporaryQueue responseQueue = jms.createTemporaryQueue();

        // send the request
        jms.createProducer()
                .setJMSReplyTo(responseQueue)
                .send(requestQueue, requestMessage);

        // start listening on the temp queue for response
        try (JMSConsumer consumer = jms.createConsumer(responseQueue)) {

            // wait for the response
            String response = consumer.receiveBody(String.class, 2000);

            if (response == null) {
                throw new IllegalStateException("Message processing timed out");
            } else {
                return response;
            }
        }
    }
}
