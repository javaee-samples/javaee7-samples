package org.javaee7.ejb.stateless;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class AccountSessionStatelessnessTest {

    final private float deposit_amount = 10f;
	private AccountSessionBean account;

	@Deployment
	public static Archive<?> deployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addClass(AccountSessionBean.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

    @Before
    public void setup() throws NamingException {
        InitialContext ctx = new InitialContext();
        Object object = ctx.lookup("java:global/test/AccountSessionBean");

        assertThat(object, instanceOf(AccountSessionBean.class));

        AccountSessionBean account = (AccountSessionBean) object;
        if (this.account != null) {
            assertThat("Expect same instance",
                    account.hashCode(),
                    is(equalTo(this.account.hashCode())));
        }

        this.account = account;
    }

	@Test
    @InSequence(1)
	public void should_deposit_amount() {
        assertThat(account.getAmount(), is(equalTo(0f)));

        String actual = account.deposit(deposit_amount);

        assertThat(actual, is(equalTo("Deposited: " + deposit_amount)));
        assertThat(account.getAmount(), is(equalTo(deposit_amount)));
	}

	@Test
    @InSequence(2)
	public void should_contain_already_deposited_amount() {
        assertThat(account.getAmount(), is(equalTo(deposit_amount)));
	}
}