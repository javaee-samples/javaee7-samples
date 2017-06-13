package org.javaee7.jms.xa;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.javaee7.jms.xa.DeliveryStats.countDownLatch;
import static org.junit.Assert.assertEquals;

import org.javaee7.jms.xa.producers.NonXAConnectionFactoryProducer;
import org.javaee7.jms.xa.utils.AbstractUserManagerTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class UserManagerNonXATest extends AbstractUserManagerTest {

    @Deployment
    public static WebArchive createDeployment() {
        return createWebArchive().addClass(NonXAConnectionFactoryProducer.class);
    }

    @Test
    public void emailAlreadyRegisteredNonXA() throws Exception {
    	
    		System.out.println("Entering emailAlreadyRegisteredNonXA");
    
        try {
            // This email is already in DB so we should get an exception trying to register it.
            userManager.register("jack@itcrowd.pl");
        } catch (Exception e) {
            logger.info("Got expected exception " + e);
        }
        
        countDownLatch.await(90, SECONDS);

        assertEquals("Timeout expired and countDownLatch did not reach 0", 0, countDownLatch.getCount());
        assertEquals("Message should be delivered despite transaction rollback", 1L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void happyPathNonXA() throws Exception {
    	
    		System.out.println("Entering happyPathNonXA");
    	
        userManager.register("bernard@itcrowd.pl");
        
        countDownLatch.await(90, SECONDS);
        
        assertEquals("Timeout expired and countDownLatch did not reach 0", 0, countDownLatch.getCount());
        assertEquals(1L, deliveryStats.getDeliveredMessagesCount());
    }
}
