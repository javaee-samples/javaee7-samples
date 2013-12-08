package org.javaee7.ejb.stateful;

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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class CartBeanStatefulnessTest {

    final private String item_to_add = "apple";
	private CartBean cartBean;

	@Deployment
	public static Archive<?> deployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar")
				.addClass(CartBean.class)
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}

    @Before
    public void setup() throws NamingException {
        InitialContext ctx = new InitialContext();
        Object object = ctx.lookup("java:global/test/CartBean");
        assertThat(object, instanceOf(CartBean.class));

        CartBean cartBean = (CartBean) object;
        if (this.cartBean != null) {
            assertThat("Expect different instances",
                    cartBean.hashCode(),
                    is(not(equalTo(this.cartBean.hashCode()))));
        }

        this.cartBean = cartBean;
    }

	@Test
    @InSequence(1)
	public void should_add_items_to_cart() {
		// when
		cartBean.addItem(item_to_add);

		// then
		assertThat(cartBean.getItems(), hasItem(item_to_add));
	}

	@Test
    @InSequence(2)
	public void should_not_contain_any_items() {
		assertThat(cartBean.getItems().isEmpty(), is(true));
	}
}