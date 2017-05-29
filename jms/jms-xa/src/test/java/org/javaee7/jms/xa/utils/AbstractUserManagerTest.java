package org.javaee7.jms.xa.utils;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;

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
                .addAsWebInfResource("ejb-jar.xml")
                .addAsWebInfResource(INSTANCE, "beans.xml")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/load.sql")
                .addClass(ReceptionSynchronizer.class)
                .addClass(AbstractUserManagerTest.class)
                .addPackage(UserManager.class.getPackage());
    }

    @Before
    public void setUp() throws Exception {
        ReceptionSynchronizer.clear();
    }
}
