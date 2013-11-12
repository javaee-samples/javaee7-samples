package org.javaee7.jms.send.receive.simple;

import org.junit.Test;
import static org.junit.Assert.*;

import javax.ejb.EJB;

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
public class SendReceiveTest {
    
    @EJB
    SimplifiedMessageSender simpleSender;
            
    @EJB
    SimplifiedMessageReceiver simpleReceiver;
    
    @EJB
    ClassicMessageSender classicSender;
    
    @EJB
    ClassicMessageReceiver classicReceiver;
    
    @Test
    public void testSimpleApi() {
        String message = "The test message";
        simpleSender.sendMessage(message);
        
        assertEquals(message, simpleReceiver.receiveMessage());
    }
    
    @Test
    public void testClassicApi() {
        String message = "The test message";
        classicSender.sendMessage(message);
        
        assertEquals(message, classicReceiver.receiveMessage());        
    }
    
    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(SimplifiedMessageReceiver.class)
                .addClass(SimplifiedMessageSender.class)
                .addClass(ClassicMessageSender.class)
                .addClass(ClassicMessageReceiver.class);
    }
    
}
