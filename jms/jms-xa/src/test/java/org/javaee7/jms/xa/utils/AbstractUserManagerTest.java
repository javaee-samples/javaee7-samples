package org.javaee7.jms.xa.utils;

import static org.javaee7.jms.xa.DeliveryStats.countDownLatch;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import javax.ejb.EJB;

import org.javaee7.jms.xa.DeliveryStats;
import org.javaee7.jms.xa.UserManager;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;

public class AbstractUserManagerTest {

    protected static final Logger logger = Logger.getLogger(AbstractUserManagerTest.class.getName());

    @EJB
    protected DeliveryStats deliveryStats;

    @EJB
    protected UserManager userManager;

    protected static WebArchive createWebArchive() {
        return create(WebArchive.class)
                .addAsWebInfResource(INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/load.sql")
                .addClass(AbstractUserManagerTest.class)
                .addPackage(UserManager.class.getPackage());
    }
    
    @Before
    public void reset() {
    		System.out.println("Resetting stats and countdown latch");
    		
    		countDownLatch = new CountDownLatch(1);
        deliveryStats.reset();
        
        assertEquals("countDownLatch should start at 1", 1, countDownLatch.getCount());
        assertEquals(0L, deliveryStats.getDeliveredMessagesCount());
    }

}
