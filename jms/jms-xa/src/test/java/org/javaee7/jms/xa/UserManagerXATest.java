package org.javaee7.jms.xa;

import org.javaee7.jms.xa.producers.XAConnectionFactoryProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class UserManagerXATest extends AbstractUserManagerTest {

    @Deployment
    public static WebArchive createDeployment()
    {
        return createWebArchive().addClass(XAConnectionFactoryProducer.class);
    }

    @Test
    public void emailAlreadyRegisteredXA() throws Exception
    {
        deliveryStats.reset();
        assertEquals(0L, deliveryStats.getDeliveredMessagesCount());
        try {
            /**
             * This email is already in DB so we should get exception trying to register it.
             */
            userManager.register("jack@itcrowd.pl");
        } catch (Exception e) {
            logger.info("Got expected exception " + e);
        }
        try {
            ReceptionSynchronizer.waitFor(JMSMailman.class, "onMessage");
            fail("Method should not have been invoked");
        } catch (AssertionError error) {
            logger.info("Got expected error " + error);
            logger.info("Message should not have been delivered due to transaction rollback");
        }
        assertEquals("Message should not be delivered due to transaction rollback", 0L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void happyPathXA() throws Exception
    {
        deliveryStats.reset();
        assertEquals(0L, deliveryStats.getDeliveredMessagesCount());
        userManager.register("bernard@itcrowd.pl");
        ReceptionSynchronizer.waitFor(JMSMailman.class, "onMessage");
        assertEquals(1L, deliveryStats.getDeliveredMessagesCount());
    }
}
