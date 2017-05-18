package org.javaee7.cdi.bean.injection;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import org.javaee7.cdi.bean.injection.beans.impl.AfterBeanDiscoveryBean;
import org.javaee7.cdi.bean.injection.beans.impl.AfterTypeDiscoveryBean;
import org.javaee7.cdi.bean.injection.beans.impl.BeforeBeanDiscoveryBean;
import org.javaee7.cdi.bean.injection.beans.impl.RegularBean;
import org.javaee7.cdi.bean.injection.extension.BeanExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Matt Gill
 */
@RunWith(Arquillian.class)
public class InjectionTest {

    @Deployment
    public static Archive<?> deploy() {

        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "org.javaee7.cdi.bean.injection")
                .addAsWebInfResource("beans.xml")
                .addAsManifestResource(new StringAsset(BeanExtension.class.getName()), "services/" + Extension.class.getName());
    }

    @Inject
    private BeforeBeanDiscoveryBean beforeBeanDiscoveryBean;

    @Inject
    private AfterTypeDiscoveryBean afterTypeDiscoveryBean;

    @Inject
    private AfterBeanDiscoveryBean afterBeanDiscoveryBean;

    @Inject
    private RegularBean regularBean;

    @Test
    public void before_bean_discovery_bean_not_null() {
        assertTrue(beforeBeanDiscoveryBean.correctlyInjected());
    }

    @Test
    public void after_type_discovery_bean_not_null() {
        assertTrue(afterTypeDiscoveryBean.correctlyInjected());
    }

    @Test
    public void after_bean_discovery_bean_not_null() {
        assertTrue(afterBeanDiscoveryBean.correctlyInjected());
    }

    @Test
    public void regular_bean_not_null() {
        assertTrue(regularBean.correctlyInjected());
    }
}
