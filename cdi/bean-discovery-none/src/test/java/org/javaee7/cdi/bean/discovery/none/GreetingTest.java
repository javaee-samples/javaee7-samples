package org.javaee7.cdi.bean.discovery.none;

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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, FancyGreeting.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    BeanManager beanManager;

    @Test
    public void should_bean_be_injected() throws Exception {
        // Cannot try to inject the bean because it would fail at deployment time (in WildFly 8)
        Set<Bean<?>> beans = beanManager.getBeans(Greeting.class);
        assertThat(beans, is(empty()));
    }
}
