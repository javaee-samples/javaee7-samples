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
import javax.transaction.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class MyTransactionalBeanWithUserTransactionTest {
    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClasses(MyTransactionalBean.class, MyTransactionScopedBean.class)
            .addAsManifestResource("beans.xml");
    }

    @Inject
    MyTransactionalBean bean;

    @Inject
    UserTransaction ut;

    @Test
    public void should_withTransaction_have_only_one_instance_injected() throws Exception {
        ut.begin();
        bean.withTransaction();
        ut.commit();
        assertThat("bean1 and bean2 should the same object", bean.id1, is(bean.id2));
    }

    @Test
    public void should_withTransaction_called_twice_have_same_instances_injected() throws Exception {
        ut.begin();
        bean.withTransaction();
        String firstId1 = bean.id1;

        bean.withTransaction();
        String secondId1 = bean.id1;
        ut.commit();

        assertThat("bean1 should change between scenarios", firstId1, is(secondId1));
    }

    @Test
    public void should_withoutTransaction_NOT_fail() throws Exception {
        try {
            ut.begin();
            bean.withoutTransaction();
            ut.commit();
        } catch (ContextNotActiveException e) {
            fail(e.toString());
        }
    }
}
