package org.javaee7.jms.xa;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;

import javax.ejb.EJB;
import java.util.logging.Logger;

public class AbstractUserManagerTest {

    protected static final Logger logger = Logger.getLogger(AbstractUserManagerTest.class.getName());

    @EJB
    protected DeliveryStats deliveryStats;

    @EJB
    protected UserManager userManager;

    protected static WebArchive createWebArchive()
    {
        return ShrinkWrap.create(WebArchive.class)
            .addAsWebInfResource("ejb-jar.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/load.sql")
            .addClass(ReceptionSynchronizer.class)
            .addPackage(UserManager.class.getPackage());
    }

    @Before
    public void setUp() throws Exception
    {
        ReceptionSynchronizer.clear();
    }
}
