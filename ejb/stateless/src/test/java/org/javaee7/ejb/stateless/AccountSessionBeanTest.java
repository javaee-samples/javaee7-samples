package org.javaee7.ejb.stateless;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Arun Gupta
 * @author Rafał Roppel
 */
@RunWith(Arquillian.class)
public class AccountSessionBeanTest {

    @Inject
    private AccountSessionBean sut;

    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(AccountSessionBean.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Test of withdraw method, of class AccountSessionBean.
     */
    @Test
    public void shouldWithdrawGivenAmount() {
        // given
        final float amount = 5.0F;

        // when
        final String actual = sut.withdraw(amount);

        // then
        assertThat(actual, is(equalTo("Withdrawn: " + amount)));
    }

    /**
     * Test of deposit method, of class AccountSessionBean.
     */
    @Test
    public void shouldDepositGivenAmount() {
        // given
        final float amount = 10.0F;

        // when
        final String actual = sut.deposit(amount);

        // then
        assertThat(actual, is(equalTo("Deposited: " + amount)));
    }
}