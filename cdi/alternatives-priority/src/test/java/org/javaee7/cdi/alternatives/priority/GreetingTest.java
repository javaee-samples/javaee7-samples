package org.javaee7.cdi.alternatives.priority;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Alexis Hassler
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, SimpleGreeting.class, FancyGreeting.class, PriorityGreeting.class)
            .addAsManifestResource("beans-empty.xml", "beans.xml");
    }

    @Inject
    Greeting bean;

    @Test
    public void should_bean_be_injected() throws Exception {
        assertThat(bean, is(notNullValue()));
    }

    @Test
    public void should_bean_be_priority() throws Exception {
        // because it has the highest priority from Priority annotated alternatives
        assertThat(bean, instanceOf(PriorityGreeting.class));
    }
}
