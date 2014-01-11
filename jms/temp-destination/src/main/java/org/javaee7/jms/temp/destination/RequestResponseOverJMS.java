package org.javaee7.jms.temp.destination;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.*;

/**
 * @author Patrik Dudits
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = Resources.REQUEST_QUEUE),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue"),
})
public class RequestResponseOverJMS implements MessageListener {

    @Inject
    JMSContext jms;

    @Override
    public void onMessage(Message message) {
        try {
            Destination replyTo = message.getJMSReplyTo();
            if (replyTo == null) {
                // no response required, finish now.
                return;
            }
            TextMessage request = (TextMessage) message;
            String payload = request.getText();

            System.out.println("Got request: "+payload);

            String response = "Processed: "+payload;
            jms.createProducer().send(replyTo, response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
