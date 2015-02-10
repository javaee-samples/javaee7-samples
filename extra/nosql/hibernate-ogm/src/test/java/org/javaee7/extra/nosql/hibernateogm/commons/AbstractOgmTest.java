package org.javaee7.extra.nosql.hibernateogm.commons;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.jboss.shrinkwrap.resolver.api.maven.Maven.resolver;
import static org.junit.Assert.assertThat;

/**
 * @author Guillaume Scheibel <guillaume.scheibel@gmail.com>
 */
public abstract class AbstractOgmTest {
    final static String OGM_VERSION = "4.0.0-SNAPSHOT";
    @PersistenceContext
    EntityManager entityManager;
    @Inject
    UserTransaction ut;

    public static WebArchive createDeployment(String ogmModuleName) {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
            .addClass(Person.class)
            .addClass(AbstractOgmTest.class)
            .addAsResource(ogmModuleName + "-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsLibraries(
                resolver().resolve(
                    "org.hibernate.ogm:hibernate-ogm-core:" + OGM_VERSION,
                    "org.hibernate.ogm:hibernate-ogm-" + ogmModuleName + ":" + OGM_VERSION
                    )
                    .withTransitivity()
                    .asFile()
            );
        return webArchive;
    }

    @Test
    public void insertEntityTest() throws Exception {
        final String name = "Guillaume";
        final Long id = 1L;
        ut.begin();
        Person guillaume = new Person(id, name);
        entityManager.persist(guillaume);
        ut.commit();

        Person person = entityManager.find(Person.class, id);
        assertThat(person, is(notNullValue()));
        assertThat(person.getId(), is(id));
        assertThat(person.getName(), is(equalTo(name)));
    }
}
