package org.javaee7.cdi.bean.discovery.annotated;

import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, SimpleGreeting.class, FancyGreeting.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    Greeting bean;

    @Test
    public void should_bean_be_injected() throws Exception {
        assertThat(bean, is(CoreMatchers.notNullValue()));
    }

    @Test
    public void should_bean_be_simple() throws Exception {
        // because SimpleGreeting is annotated (scope)
        assertThat(bean, instanceOf(SimpleGreeting.class));
    }
}
