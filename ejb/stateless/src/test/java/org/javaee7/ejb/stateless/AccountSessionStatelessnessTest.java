package org.javaee7.ejb.stateless;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class AccountSessionStatelessnessTest {

    @EJB
    AccountSessionBean account1;

    @EJB
    AccountSessionBean account2;

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addClass(AccountSessionBean.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * JSR 318: Enterprise JavaBeans, Version 3.1
     * 3.4.7.2 Session Object Identity / Stateless Session Beans
     *
     * All business object references of the same interface type for the same
     * stateless session bean have the same object identity, which is assigned
     * by the container. All references to the no-interface view of the same
     * stateless session bean have the same object identity.
     */
    @Test
    public void should_be_identical_beans() {
        assertThat("Expect same instances", account1, is(account2));
    }
}
