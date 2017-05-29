package org.javaee7.jms.xa;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.JMSConnectionFactoryDefinitions;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;

@JMSDestinationDefinition(
    name = "java:app/jms/queue", 
    interfaceName = "javax.jms.Queue"
)
@JMSConnectionFactoryDefinitions(
    value = {
        // Will be selected via the NonXAConnectionFactoryProducer
        @JMSConnectionFactoryDefinition(
            name = "java:app/jms/nonXAconnectionFactory",
            transactional = false
        ),
        
        // Will be selected via the XAConnectionFactoryProducer
        @JMSConnectionFactoryDefinition(
            name = "java:app/jms/xaConnectionFactory"
        )
    }        
)
@Singleton
public class JMSSender {

    @Inject
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:app/jms/queue")
    private Queue queue;

    public void sendMessage(String payload) {
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(queue, payload);
        }
    }
}
