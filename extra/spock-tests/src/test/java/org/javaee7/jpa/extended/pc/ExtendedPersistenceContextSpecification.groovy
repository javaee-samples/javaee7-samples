package org.javaee7.jpa.extended.pc

import org.javaee7.jpa.extended.pc.Character
import org.javaee7.jpa.extended.pc.CharactersBean
import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.spock.ArquillianSputnik
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.runner.RunWith
import spock.lang.Specification

import javax.ejb.EJB
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver

/**
 * @author Kuba Marchwicki
 */
@RunWith(ArquillianSputnik)
class ExtendedPersistenceContextSpecification extends Specification {

    @PersistenceContext
    EntityManager em;

    @EJB
    CharactersBean bean;

    @Deployment
    def static WebArchive deploy() {
        ShrinkWrap.create(WebArchive.class)
                .addAsLibraries(
                    resolver().resolve("org.javaee7:jpa-extended-pc:jar:classes:1.0-SNAPSHOT")
                            .withTransitivity().asFile()
                );
    }

    def setup() {
        Character wil = new Character(8, "Wil Wheaton")
        bean.save(wil)

        for (Character c : bean.get()) {
            if ("Raj".equals(c.getName())) {
                c.setName("Rajesh Ramayan")
                bean.save(c)
            }
        }
    }

    def "should not persist changes without transaction flush"() {
        expect:
        7 == em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList().size();
        "Raj" == em.find(Character.class, 6).name
    }

    def "should update characters after transaction flush"() {
        when:
        bean.commitChanges()

        then:
        8 == em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList().size();
        "Rajesh Ramayan" == em.find(Character.class, 6).name
        "Wil Wheaton" == em.find(Character.class, 8).name
    }
}
