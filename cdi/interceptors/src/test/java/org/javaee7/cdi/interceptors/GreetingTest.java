package org.javaee7.cdi.interceptors;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, SimpleGreeting.class, MyInterceptorBinding.class, MyInterceptor.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    Greeting bean;

    @Test
    public void test() throws Exception {
        assertThat(bean, is(notNullValue()));
        assertThat(bean, instanceOf(SimpleGreeting.class));

        bean.setGreet("Arun");
        assertEquals(bean.getGreet(), "Hi Arun !");
    }
}
