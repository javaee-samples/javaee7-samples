package org.javaee7.jms.send.receive.simple.appmanaged;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

import org.javaee7.jms.send.receive.Resources;

/**
 * Synchronous message sending with app-managed JMSContext.
 * JMSContext can be used with try-with-resources construct.
 * @author Arun Gupta
 */
@Stateless
public class MessageSenderAppManaged {

    @Resource
    private ConnectionFactory factory;

    @Resource(mappedName = Resources.SYNC_APP_MANAGED_QUEUE)
    Queue myQueue;

    public void sendMessage(String message) {
        try (JMSContext context = factory.createContext()) {
            context.createProducer().send(myQueue, message);
        }
    }
}
