package org.javaee7.interceptor.aroundconstruct;

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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Radim Hanus
 */
@RunWith(Arquillian.class)
public class GreetingBeanTest {
    @Inject
    private Greeting bean;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(Greeting.class, GreetingBean.class, GreetingParam.class, MyInterceptor.class, MyInterceptorBinding.class, Param.class)
            .addAsManifestResource("beans.xml");
    }

    @Test
    public void should_be_ready() throws Exception {
        assertThat(bean, is(notNullValue()));
        assertThat(bean, instanceOf(GreetingBean.class));
        assertTrue(bean.isConstructed());
        assertTrue(bean.isInitialized());
        assertThat(bean.getParam(), instanceOf(GreetingParam.class));
    }
}
