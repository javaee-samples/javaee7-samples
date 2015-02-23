package org.javaee7.jta.transactional;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.transaction.TransactionalException;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class MyTransactionalTxTypeBeanTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(MyTransactionalTxTypeBean.class);
    }

    @Inject
    MyTransactionalTxTypeBean bean;

    @Test
    public void should_required_work() {
        bean.required();
    }

    @Test
    public void should_requiresNew_work() {
        bean.requiresNew();
    }

    @Test(expected = TransactionalException.class)
    public void should_mandatory_throw_exception() {
        bean.mandatory();
    }

    @Test
    public void should_supports_work() {
        bean.supports();
    }

    @Test
    public void should_notSupported_work() {
        bean.notSupported();
    }

    @Test
    public void should_never_work() {
        bean.never();
    }
}
