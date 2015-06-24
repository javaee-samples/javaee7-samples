package org.javaee7.jpa.extended.pc;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class ExtendedPersistenceContextTest {

    @PersistenceContext
    EntityManager em;

    @EJB
    CharactersBean bean;

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage("org.javaee7.jpa.extended.pc")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/load.sql");
    }

    @Before
    public void setup() {
        Character wil = new Character(8, "Wil Wheaton");
        bean.save(wil);

        for (Character c : bean.get()) {
            if ("Raj".equals(c.getName())) {
                c.setName("Rajesh Ramayan");
                bean.save(c);
            }
        }
    }

    @Test
    @InSequence(1)
    public void should_not_persist_changes_without_transaction_flush() {
        List<Character> characters = em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList();
        Character raj = em.find(Character.class, 6);

        assertThat(characters, hasSize(7));
        assertThat(raj.getName(), is(equalTo("Raj")));
    }

    @Test
    @InSequence(2)
    public void should_update_characters_after_transaction_flush() {
        //when
        bean.commitChanges();

        //then
        List<Character> characters = em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList();
        Character rajesh = em.find(Character.class, 6);
        Character wil = em.find(Character.class, 8);

        assertThat(characters, hasSize(8));
        assertThat(rajesh.getName(), is(equalTo("Rajesh Ramayan")));
        assertThat(wil.getName(), is(equalTo("Wil Wheaton")));
    }


}
