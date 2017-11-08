package org.javaee7.jms.send.receive.simple.appmanaged;

import java.util.concurrent.TimeoutException;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;
import org.javaee7.jms.send.receive.Resources;

/**
 * @author Arun Gupta
 */
@Stateless
public class MessageReceiverAppManaged {

    @Resource
    private ConnectionFactory factory;

    @Resource(mappedName = Resources.SYNC_APP_MANAGED_QUEUE)
    Queue myQueue;

    /**
     * Waits to receive a message from the JMS queue. Times out after a given
     * number of milliseconds.
     *
     * @param timeoutInMillis The number of milliseconds this method will wait
     * before throwing an exception.
     * @return The contents of the message.
     * @throws JMSRuntimeException if an error occurs in accessing the queue.
     * @throws TimeoutException if the timeout is reached.
     */
    public String receiveMessage(int timeoutInMillis) throws JMSRuntimeException, TimeoutException {
        try (JMSContext context = factory.createContext()) {
            String message = context.createConsumer(myQueue).receiveBody(String.class, timeoutInMillis);
            if (message == null) {
                throw new TimeoutException("No message received after " + timeoutInMillis + "ms");
            }
            return message;
        }
    }
}
