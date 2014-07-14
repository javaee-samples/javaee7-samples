package org.javaee7.jms.send.receive.simple;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;

import org.javaee7.jms.send.receive.Resources;

/**
 * Synchronous message reception with container-managed JMSContext.
 * @author Arun Gupta
 */
@Stateless
public class MessageReceiverSync {

    @Inject
    private JMSContext context;
    
    @Resource(mappedName=Resources.SYNC_CONTAINER_MANAGED_QUEUE)
    Queue myQueue;

    public String receiveMessage() {
        return context.createConsumer(myQueue).receiveBody(String.class, 1000);
    }
    
    public void receiveAll() {
        System.out.println("--> Receiving redundant messages ...");
        try {
            QueueBrowser browser = context.createBrowser(myQueue);
            while (browser.getEnumeration().hasMoreElements()) {
                System.out.println("--> here is one");
                context.createConsumer(myQueue).receiveBody(String.class, 1000);
            }
        } catch (JMSException ex) {
            Logger.getLogger(MessageReceiverSync.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
