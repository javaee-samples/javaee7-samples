package org.javaee7.jms.xa;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.javaee7.jms.xa.DeliveryStats.countDownLatch;
import static org.junit.Assert.assertEquals;

import org.javaee7.jms.xa.producers.XAConnectionFactoryProducer;
import org.javaee7.jms.xa.utils.AbstractUserManagerTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserManagerXATest extends AbstractUserManagerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return createWebArchive().addClass(XAConnectionFactoryProducer.class);
    }
    
    @Test
    public void emailAlreadyRegisteredXA() throws Exception {
        try {
            // This email is already in the DB so we should get an exception trying to register it.
            userManager.register("jack@itcrowd.pl");
        } catch (Exception e) {
            logger.info("Got expected exception " + e);
        }
        
        // Wait for at most 30 seconds for the JMS method to NOT be called, since we're testing for something
        // to NOT happen we can never be 100% sure, but 30 seconds should cover almost all cases.
        countDownLatch.await(30, SECONDS);
        
        assertEquals("countDownLatch was decreased meaning JMS method was called, but should not have been.", 1, countDownLatch.getCount());
        assertEquals("Message should not be delivered due to transaction rollback", 0L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void happyPathXA() throws Exception {
        userManager.register("bernard@itcrowd.pl");
        
        // Wait for at most 90 seconds for the JMS method to be called
        countDownLatch.await(90, SECONDS);
        
        assertEquals("Timeout expired and countDownLatch did not reach 0 (so JMS method not called)", 0, countDownLatch.getCount());
        assertEquals(1L, deliveryStats.getDeliveredMessagesCount());
    }
}
