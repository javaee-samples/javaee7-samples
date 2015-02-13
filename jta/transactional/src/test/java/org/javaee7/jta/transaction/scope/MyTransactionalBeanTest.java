package org.javaee7.jta.transaction.scope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.context.ContextNotActiveException;
import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class MyTransactionalBeanTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(MyTransactionalBean.class, MyTransactionScopedBean.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    MyTransactionalBean bean;

    @Test
    public void should_withTransaction_have_only_one_instance_injected() {
        bean.withTransaction();
        assertThat("bean1 and bean2 should the same object", bean.id1, is(bean.id2));
    }

    @Test
    public void should_withTransaction_called_twice_have_different_instances_injected() {
        bean.withTransaction();
        String firstId1 = bean.id1;

        bean.withTransaction();
        String secondId1 = bean.id1;

        assertThat("bean1 should change between scenarios", firstId1, is(not(secondId1)));
    }

    @Test
    public void should_withoutTransaction_fail() {
        try {
            bean.withoutTransaction();
            fail("No ContextNotActiveException");
        } catch (ContextNotActiveException e) {
        }
    }

}
