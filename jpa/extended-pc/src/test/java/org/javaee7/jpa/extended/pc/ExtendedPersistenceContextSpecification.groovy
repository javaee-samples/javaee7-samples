package org.javaee7.jpa.extended.pc

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.spock.ArquillianSputnik
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.runner.RunWith
import spock.lang.Specification

import javax.inject.Inject
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * @author Kuba Marchwicki
 */
@RunWith(ArquillianSputnik)
class ExtendedPersistenceContextSpecification extends Specification {

    @PersistenceContext
    EntityManager em;

    @Inject
    BigBangTheoryService service;

    @Deployment
    def static WebArchive deploy() {
        ShrinkWrap.create(WebArchive.class)
                .addPackage("org.javaee7.jpa.extended.pc")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/load.sql");
    }

    def setup() {
        service.addWilWheaton();
        service.updateRaj();
    }

    def "should not persist changes without transaction flush"() {
        expect:
        7 == em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList().size();
        "Raj" == em.find(Character.class, 6).name
    }

    def "should update characters after transaction flush"() {
        when:
        service.proceed();

        then:
        8 == em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList().size();
        "Rajesh Ramayan" == em.find(Character.class, 6).name
        "Wil Wheaton" == em.find(Character.class, 8).name
    }
}
