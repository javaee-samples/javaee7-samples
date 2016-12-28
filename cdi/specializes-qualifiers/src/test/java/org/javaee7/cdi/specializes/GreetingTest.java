package org.javaee7.cdi.specializes;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Personal.class, Greeting.class, SpecializedGreeting.class)
                .addAsManifestResource("beans.xml");
    }

    @Inject
    @Personal
    private Greeting personalBean;

    @Inject
    @Named("base")
    private Greeting simpleBean;

    @Inject
    @Any
    private Instance<Greeting> instance;

    @Test
    public void beans_should_be_specialized() throws Exception {
        // specialized implementation automatically inherits all qualifiers of the base implementation
        assertThat(personalBean, instanceOf(SpecializedGreeting.class));
        assertThat(simpleBean, instanceOf(SpecializedGreeting.class));
    }

    @Test
    public void default_bean_should_not_be_available() throws Exception {
        // specialized implementation inherited some qualifiers so that Default has not been set
        Instance<Greeting> defaultInstance = instance.select(new AnnotationLiteral<Default>() {});
        assertTrue(defaultInstance.isUnsatisfied());
    }
}
