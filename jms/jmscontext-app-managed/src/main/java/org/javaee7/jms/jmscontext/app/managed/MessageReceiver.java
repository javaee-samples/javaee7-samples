package org.javaee7.jms.jmscontext.app.managed;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;

/**
 * @author Arun Gupta
 */
@Stateless
public class MessageReceiver {

    @Resource
    private ConnectionFactory factory;
    
    @Resource(mappedName="java:global/jms/myQueue")
    Queue myQueue;

    public String receiveMessage() {
        try (JMSContext context = factory.createContext()) {
            return context.createConsumer(myQueue).receiveBody(String.class, 1000);
        }
    }
}
