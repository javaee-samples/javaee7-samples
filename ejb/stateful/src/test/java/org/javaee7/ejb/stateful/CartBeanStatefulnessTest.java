package org.javaee7.ejb.stateful;

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

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @author Jakub Marchwicki
 */
@RunWith(Arquillian.class)
public class CartBeanStatefulnessTest {

    final private String item_to_add = "apple";

    @EJB
    private CartBean bean1;

    @EJB
    private CartBean bean2;

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar")
            .addClass(CartBean.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * JSR 318: Enterprise JavaBeans, Version 3.1
     * 3.4.7.1 Session Object Identity / Stateful Session Beans
     *
     * A stateful session object has a unique identity that is assigned by
     * the container at the time the object is created. A client of the stateful
     * session bean business interface can determine if two business interface
     * or no-interface view references refer to the same session object
     * by use of the equals method
     */
    @Test
    @InSequence(1)
    public void should_not_be_identical_beans() {
        assertThat("Expect different instances", bean1, is(not(bean2)));
    }

    @Test
    @InSequence(2)
    public void should_add_items_to_first_cart() {
        // when
        bean1.addItem(item_to_add);

        // then
        assertThat(bean1.getItems(), hasItem(item_to_add));
    }

    @Test
    @InSequence(3)
    public void should_not_contain_any_items_in_second_cart() {
        assertThat(bean2.getItems().isEmpty(), is(true));
    }
}