package org.javaee7.cdi.interceptors.priority;

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
 * Note that beans.xml doesn't define any interceptor. Interceptors declared using interceptor bindings
 * are enabled for the entire application and ordered using the Priority annotation.
 *
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, SimpleGreeting.class, MyInterceptorBinding.class, LowPriorityInterceptor.class, HighPriorityInterceptor.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    Greeting bean;

    @Test
    public void test() throws Exception {
        assertThat(bean, is(notNullValue()));
        assertThat(bean, instanceOf(SimpleGreeting.class));

        bean.setGreet("Arun");
        assertEquals(bean.getGreet(), "Hi Arun ! Nice to meet you.");
    }
}
