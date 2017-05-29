package org.javaee7.jms.xa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.javaee7.jms.xa.producers.XAConnectionFactoryProducer;
import org.javaee7.jms.xa.utils.AbstractUserManagerTest;
import org.javaee7.jms.xa.utils.ReceptionSynchronizer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserManagerXATest extends AbstractUserManagerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return createWebArchive().addClass(XAConnectionFactoryProducer.class);
    }

    @Before
    public void reset() {
        deliveryStats.reset();
        assertEquals(0L, deliveryStats.getDeliveredMessagesCount());
    }
    
    @Test
    public void emailAlreadyRegisteredXA() throws Exception {
        try {
            // This email is already in the DB so we should get an exception trying to register it.
            userManager.register("jack@itcrowd.pl");
        } catch (Exception e) {
            logger.info("Got expected exception " + e);
        }
        
        try {
            ReceptionSynchronizer.waitFor(JMSListener.class, "onMessage");
            fail("Method should not have been invoked");
        } catch (AssertionError error) {
            logger.info("Got expected error " + error);
            logger.info("Message should not have been delivered due to transaction rollback");
        }
        
        assertEquals("Message should not be delivered due to transaction rollback", 0L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void happyPathXA() throws Exception {
        userManager.register("bernard@itcrowd.pl");
        ReceptionSynchronizer.waitFor(JMSListener.class, "onMessage");
        
        assertEquals(1L, deliveryStats.getDeliveredMessagesCount());
    }
}
