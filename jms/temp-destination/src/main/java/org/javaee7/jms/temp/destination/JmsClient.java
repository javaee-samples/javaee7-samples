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
public class JmsClient {

    @Resource(lookup = Resources.REQUEST_QUEUE)
    Queue requestQueue;

    @Inject
    JMSContext jms;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    // <1> we need to send message in the middle of the method, therefore we cannot be transactional
        public
        String process(String request) {

        TextMessage requestMessage = jms.createTextMessage(request);
        TemporaryQueue responseQueue = jms.createTemporaryQueue();
        jms.createProducer()
            .setJMSReplyTo(responseQueue) // <2> set the temporary queue as replyToDestination
            .send(requestQueue, requestMessage); // <3> immediately send the request message

        try (JMSConsumer consumer = jms.createConsumer(responseQueue)) { // <4> listen on the temporary queue

            String response = consumer.receiveBody(String.class, 2000); // <5> wait for a +TextMessage+ to arrive

            if (response == null) { // <6> +receiveBody+  returns +null+ in case of timeout
                throw new IllegalStateException("Message processing timed out");
            } else {
                return response;
            }
        }
    }
}
