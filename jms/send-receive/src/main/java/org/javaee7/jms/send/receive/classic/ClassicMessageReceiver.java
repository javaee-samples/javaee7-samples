package org.javaee7.jms.send.receive.classic;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

import org.javaee7.jms.send.receive.Resources;

/**
 * Synchronized message receiver using classic API.
 * @author Arun Gupta
 */
@Stateless
public class ClassicMessageReceiver {

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    ConnectionFactory connectionFactory;
    
    @Resource(mappedName = Resources.CLASSIC_QUEUE)
    Queue demoQueue;

    public String receiveMessage() {
        String response = null;
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer messageConsumer = session.createConsumer(demoQueue);
            Message message = messageConsumer.receive(5000);
            response = message.getBody(String.class);
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
        return response;
    }
}
