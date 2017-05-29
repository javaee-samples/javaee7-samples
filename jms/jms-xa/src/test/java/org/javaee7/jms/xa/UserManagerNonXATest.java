package org.javaee7.jms.xa;

import static org.junit.Assert.assertEquals;

import org.javaee7.jms.xa.producers.NonXAConnectionFactoryProducer;
import org.javaee7.jms.xa.utils.AbstractUserManagerTest;
import org.javaee7.jms.xa.utils.ReceptionSynchronizer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserManagerNonXATest extends AbstractUserManagerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return createWebArchive().addClass(NonXAConnectionFactoryProducer.class);
    }
    
    @Before
    public void reset() {
        deliveryStats.reset();
        assertEquals(0L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void emailAlreadyRegisteredNonXA() throws Exception {
        try {
            
            // This email is already in DB so we should get an exception trying to register it.
             
            userManager.register("jack@itcrowd.pl");
        } catch (Exception e) {
            logger.info("Got expected exception " + e);
        }
        
        try {
            ReceptionSynchronizer.waitFor(JMSListener.class, "onMessage");
        } catch (AssertionError error) {
            logger.info("Got expected error " + error);
            logger.info("We're just making sure that we have waited long enough to let the message get to MDB");
        }
        
        assertEquals("Message should be delivered despite transaction rollback", 1L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void happyPathNonXA() throws Exception {
        userManager.register("bernard@itcrowd.pl");
        
        try {
            ReceptionSynchronizer.waitFor(JMSListener.class, "onMessage");
        } catch (AssertionError error) {
            logger.info("Got expected error " + error);
            logger.info("We're just making sure that we have waited long enough to let the message get to MDB");
        }
        
        assertEquals(1L, deliveryStats.getDeliveredMessagesCount());
    }
}
