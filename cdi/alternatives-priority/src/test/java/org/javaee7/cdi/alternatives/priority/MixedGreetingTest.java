package org.javaee7.cdi.alternatives.priority;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class MixedGreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, SimpleGreeting.class, FancyGreeting.class)
            .addAsManifestResource("beans-alternatives.xml", "beans.xml");
    }

    @Inject
    BeanManager beanManager;

    @Test
    public void should_be_ambiguous() throws Exception {
        Set<Bean<?>> beans = beanManager.getBeans(Greeting.class);
        assertTrue(beans.size() == 2);
    }
}
