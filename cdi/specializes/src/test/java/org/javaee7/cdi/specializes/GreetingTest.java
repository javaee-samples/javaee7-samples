package org.javaee7.cdi.specializes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Greeting.class, SpecializedGreeting.class)
                .addAsManifestResource("beans.xml");
    }

    @Inject
    private Greeting bean;

    @Test
    public void should_bean_be_specialized() throws Exception {
        assertThat(bean, instanceOf(SpecializedGreeting.class));
    }
}
