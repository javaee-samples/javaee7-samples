package org.javaee7.cdi.bean.injection;

import javax.enterprise.inject.spi.Extension;
import javax.inject.Inject;
import org.javaee7.cdi.bean.injection.beans.impl.AfterBeanDiscoveryBean;
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
                .addAsManifestResource("beans.xml")
                .addAsWebInfResource("beans.xml")
                .addAsManifestResource("persistence.xml")
                .addAsManifestResource(new StringAsset(BeanExtension.class.getName()), "services/" + Extension.class.getName());
    }

    @Inject
    private AfterBeanDiscoveryBean bean;

    @Test
    public void after_bean_discovery_bean_not_null() {
        assertTrue(bean != null);
    }
}
