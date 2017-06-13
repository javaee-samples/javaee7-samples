package org.javaee7.jms.xa;

import static java.util.logging.Level.SEVERE;
import static org.javaee7.jms.xa.DeliveryStats.countDownLatch;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
    activationConfig = { 
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/queue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), }
)
public class JMSListener implements MessageListener {

    private static final Logger logger = Logger.getLogger(JMSListener.class.getName());

    @EJB
    private DeliveryStats deliveryStats;

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("Message received (async): " + message.getBody(String.class));
            
            deliveryStats.messageDelivered();
            countDownLatch.countDown();
        } catch (JMSException ex) {
            logger.log(SEVERE, null, ex);
        }
    }
}
