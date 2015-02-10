package org.javaee7.jms.send.receive.classic;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.javaee7.jms.send.receive.Resources;

/**
 * Sending a message using classic JMS API.
 * @author Arun Gupta
 */
@Stateless
public class ClassicMessageSender {

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    ConnectionFactory connectionFactory;

    @Resource(mappedName = Resources.CLASSIC_QUEUE)
    Queue demoQueue;

    public void sendMessage(String payload) {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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
