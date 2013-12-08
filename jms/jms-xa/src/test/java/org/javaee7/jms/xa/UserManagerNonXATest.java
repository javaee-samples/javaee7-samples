package org.javaee7.jms.xa;

import org.javaee7.jms.xa.producers.ConnectionFactoryProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class UserManagerNonXATest extends AbstractUserManagerTest {

    @Deployment
    public static WebArchive createDeployment()
    {
        return createWebArchive().addClass(ConnectionFactoryProducer.class);
    }

    @Test
    public void emailAlreadyRegisteredNonXA() throws Exception
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
        } catch (AssertionError error) {
            logger.info("Got expected error " + error);
            logger.info("We're just making sure that we have waited long enough to let the message get to MDB");
        }
        assertEquals("Message should be delivered despite transaction rollback", 1L, deliveryStats.getDeliveredMessagesCount());
    }

    @Test
    public void happyPathNonXA() throws Exception
    {
        deliveryStats.reset();
        assertEquals(0L, deliveryStats.getDeliveredMessagesCount());
        userManager.register("bernard@itcrowd.pl");
        try {
            ReceptionSynchronizer.waitFor(JMSMailman.class, "onMessage");
        } catch (AssertionError error) {
            logger.info("Got expected error " + error);
            logger.info("We're just making sure that we have waited long enough to let the message get to MDB");
        }
        assertEquals(1L, deliveryStats.getDeliveredMessagesCount());
    }
}
