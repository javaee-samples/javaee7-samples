package org.javaee7.cdi.beansxml.noversion;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(AnnotatedBean.class, NotAnnotatedBean.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    AnnotatedBean annotatedBean;
    @Inject
    NotAnnotatedBean notAnnotatedBean;

    @Test
    public void should_bean_be_injected() throws Exception {
        assertThat(annotatedBean, is(notNullValue()));

        // notAnnotatedBean is injected because CDI acts as version 1.0 if version is not explicit
        assertThat(notAnnotatedBean, is(notNullValue()));
    }
}
