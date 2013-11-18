package org.javaee7.jms.send.receive;

import org.javaee7.jms.send.receive.classic.ClassicMessageSender;
import org.javaee7.jms.send.receive.classic.ClassicMessageReceiver;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.ejb.EJB;

import org.javaee7.jms.send.receive.simple.MessageReceiverSync;
import org.javaee7.jms.send.receive.simple.MessageSenderSync;
import org.javaee7.jms.send.receive.simple.appmanaged.MessageSenderAppManaged;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

/**
 *
 * @author Patrik Dudits
 */
@RunWith(Arquillian.class)
public class SyncTest {
    
    @EJB
    MessageSenderSync simpleSender;
            
    @EJB
    MessageReceiverSync simpleReceiver;
    
    @EJB
    ClassicMessageSender classicSender;
    
    @EJB
    ClassicMessageReceiver classicReceiver;
    
    @EJB
    MessageSenderAppManaged appManaged;
    
    @Test
    public void testSimpleApi() {
        String message = "The test message over JMS 2.0 API";
        simpleSender.sendMessage(message);
        
        assertEquals(message, simpleReceiver.receiveMessage());
    }
    
    @Test
    public void testClassicApi() {
        String message = "The test message over JMS 1.1 API";
        classicSender.sendMessage(message);
        
        assertEquals(message, classicReceiver.receiveMessage());        
    }
    
    @Test
    public void testAppManagedJmsContext() {
        String message = "The test message over app-managed JMSContext";
        appManaged.sendMessage(message);
        
        assertEquals(message, simpleReceiver.receiveMessage());        
    }
    
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(MessageSenderSync.class)
                .addClass(MessageReceiverSync.class)
                .addClass(ClassicMessageSender.class)
                .addClass(ClassicMessageReceiver.class)
                .addClass(MessageSenderAppManaged.class)
                .addClass(Resources.class);
    }
    
}
