package org.javaee7.jms.xa;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

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
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.SESSION_TRANSACTED);
            MessageProducer messageProducer = session.createProducer(demoQueue);
            TextMessage textMessage = session.createTextMessage(payload);
            messageProducer.send(textMessage);
        } catch (JMSException ex) {
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
