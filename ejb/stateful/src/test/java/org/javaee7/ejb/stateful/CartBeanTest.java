package org.javaee7.ejb.stateful;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Rafał Roppel
 */
@RunWith(Arquillian.class)
public class CartBeanTest {

    @Inject
    private CartBean sut;

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addClass(CartBean.class)
            .addAsManifestResource(INSTANCE, "beans.xml");
    }

    /**
     * Test of addItem method, of class CartBean
     *
     * @throws Exception
     */
    @Test
    public void shouldAddOneItem() throws Exception {
        // given

        // when
        sut.addItem("apple");

        // then
        assertThat(sut.getItems(), hasItem("apple"));
    }

    /**
     * Test of addItem method, of class CartBean
     *
     * @throws Exception
     */
    @Test
    public void shouldAddManyItems() throws Exception {
        // given
        final List<String> items = Arrays.asList("apple", "banana", "mango", "kiwi", "passion fruit");

        // when
        for (final String item : items) {
            sut.addItem(item);
        }

        // then
        assertThat(sut.getItems(), is(items));
    }

    /**
     * Test of removeItem method, of class CartBean
     *
     * @throws Exception
     */
    @Test
    public void shouldRemoveOneItem() throws Exception {
        // given
        final List<String> items = Arrays.asList("apple", "banana", "mango", "kiwi", "passion fruit");
        for (final String item : items) {
            sut.addItem(item);
        }

        // when
        sut.removeItem("banana");

        // then
        assertThat(sut.getItems(), not(hasItem("banana")));
    }

    /**
     * Test of getItems method, of class CartBean
     *
     * @throws Exception
     */
    @Test
    public void shouldBeEmpty() throws Exception {
        // given

        // when
        final List<String> actual = sut.getItems();

        // then
        assertThat(actual.isEmpty(), is(true));
    }
}