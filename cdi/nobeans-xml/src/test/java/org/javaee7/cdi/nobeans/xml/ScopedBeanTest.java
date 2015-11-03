package org.javaee7.cdi.nobeans.xml;

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
public class ScopedBeanTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(ScopedBean.class);
    }

    @Inject
    ScopedBean bean;

    @Test
    public void should_scope_bean_be_injected() throws Exception {
        assertThat(bean, is(notNullValue()));
    }
}
