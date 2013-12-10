package org.javaee7.jms.xa;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "destinationLookup",
    propertyValue = Mailman.CLASSIC_QUEUE), @ActivationConfigProperty(propertyName = "destinationType",
    propertyValue = "javax.jms.Queue"),})
public class JMSMailman implements MessageListener {

    private static final Logger logger = Logger.getLogger(JMSMailman.class.getName());

    @EJB
    private DeliveryStats deliveryStats;

    @Override
    public void onMessage(Message message)
    {
        try {
            TextMessage tm = (TextMessage) message;
            logger.info("Message received (async): " + tm.getText());
            deliveryStats.messageDelivered();
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
