package org.javaee7.ejb.stateless;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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

    final private float deposit_amount = 10f;

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
    @InSequence(1)
    public void should_be_identical_beans() {
        assertThat("Expect same instances", account1, is(account2));
    }

	@Test
    @InSequence(2)
	public void should_deposit_amount_on_first_account() {
        assertThat(account1.getAmount(), is(equalTo(0f)));

        String actual = account1.deposit(deposit_amount);

        assertThat(actual, is(equalTo("Deposited: " + deposit_amount)));
        assertThat(account1.getAmount(), is(equalTo(deposit_amount)));
	}

	@Test
    @InSequence(3)
	public void should_contain_already_deposited_amount_on_second_account() {
        assertThat(account2.getAmount(), is(equalTo(deposit_amount)));
	}
}