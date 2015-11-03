package org.javaee7.jms.xa;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destinationLookup",
    propertyValue = Mailman.CLASSIC_QUEUE), @ActivationConfigProperty(propertyName = "destinationType",
    propertyValue = "javax.jms.Queue"), })
public class JMSMailman implements MessageListener {

    private static final Logger logger = Logger.getLogger(JMSMailman.class.getName());

    @EJB
    private DeliveryStats deliveryStats;

    @Override
    public void onMessage(Message message)
    {
        try {
            String text = message.getBody(String.class);
            logger.info("Message received (async): " + text);
            deliveryStats.messageDelivered();
        } catch (JMSException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }
}
