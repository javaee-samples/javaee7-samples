package org.javaee7.jpa.listeners

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.spock.ArquillianSputnik
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.runner.RunWith
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

@RunWith(ArquillianSputnik)
class JpaListenerInjectionSpecification extends Specification {

    @Deployment
    def static WebArchive "create deployment"() {
        ShrinkWrap.create(WebArchive.class)
                .addPackage("org.javaee7.jpa.listeners")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/load.sql");
    }

    @Inject
    MovieBean bean;

    @Unroll("should provide a movie rating of #rating for movie #name")
    def "provide movie rating via jpa listener injection"() {
        expect:
        rating == bean.getMovieByName(name).rating

        where:
        name                    | rating
        "The Matrix"            | 60
        "The Lord of The Rings" | 70
        "Inception"             | 80
        "The Shining"           | 90
    }

}
