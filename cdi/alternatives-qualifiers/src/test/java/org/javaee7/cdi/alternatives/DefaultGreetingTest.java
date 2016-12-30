package org.javaee7.cdi.alternatives;

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
public class DefaultGreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Greeting.class, DefaultGreeting.class, AlternativeGreeting.class,
                        Personal.class, PersonalGreeting.class, AlternativePersonalGreeting.class)
                .addAsManifestResource("beans-empty.xml", "beans.xml");
    }

    @Inject
    private Greeting greeting;

    @Inject
    @Personal
    private Greeting personalGreeting;

    @Test
    public void should_greeting_be_default() throws Exception {
        // default implementation because of empty beans.xml
        assertThat(greeting, instanceOf(DefaultGreeting.class));
    }

    @Test
    public void should_personal_greeting_be_default() throws Exception {
        // qualified implementation because of empty beans.xml
        assertThat(personalGreeting, instanceOf(PersonalGreeting.class));
    }
}
