package org.javaee7.jms.send.receive.simple;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.CompletionListener;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

import org.javaee7.jms.send.receive.Resources;

/**
 * Asynchronous message sending is not supported in Java EE 7.
 * @author Arun Gupta
 */
@Stateless
public class MessageSenderAsync {

    @Inject
    //    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    JMSContext context;

    @Resource(lookup = Resources.ASYNC_QUEUE)
    Queue asyncQueue;

    public void sendMessage(String message) {
        try {
            context.createProducer().setAsync(new CompletionListener() {
                @Override
                public void onCompletion(Message msg) {
                    try {
                        System.out.println(msg.getBody(String.class));
                    } catch (JMSException ex) {
                        Logger.getLogger(MessageSenderAsync.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                @Override
                public void onException(Message msg, Exception e) {
                    try {
                        System.out.println(msg.getBody(String.class));
                    } catch (JMSException ex) {
                        Logger.getLogger(MessageSenderAsync.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        } catch (RuntimeException e) {
            System.out.println("Caught RuntimeException trying to invoke setAsync - not permitted in Java EE");
        }

        context.createProducer().send(asyncQueue, message);
    }
}
