package org.javaee7.jms.xa;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Queue;

@JMSDestinationDefinition(
    name = Mailman.CLASSIC_QUEUE,
    resourceAdapter = "jmsra",
    interfaceName = "javax.jms.Queue",
    destinationName = "classicQueue",
    description = "My Sync Queue")
@Singleton
public class Mailman {

    public static final String CLASSIC_QUEUE = "java:jboss/jms/classicQueue";

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    ConnectionFactory connectionFactory;

    @Resource(mappedName = CLASSIC_QUEUE)
    Queue demoQueue;

    public void sendMessage(String payload)
    {
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(demoQueue, payload);
        }
    }
}
