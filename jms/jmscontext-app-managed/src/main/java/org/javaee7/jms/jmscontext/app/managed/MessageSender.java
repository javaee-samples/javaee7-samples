package org.javaee7.jms.jmscontext.app.managed;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;

/**
 * @author Arun Gupta
 */
@Stateless
@JMSDestinationDefinition(name = "java:global/jms/myQueue",
        interfaceName = "javax.jms.Queue")
public class MessageSender {

    @Resource
    private ConnectionFactory factory;
    
    @Resource(mappedName="java:global/jms/myQueue")
    Queue myQueue;

    public void sendMessage(String message) {
        try (JMSContext context = factory.createContext()) {
            context.createProducer().send(myQueue, message);
        }
    }
}
