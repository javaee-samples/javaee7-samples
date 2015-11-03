package org.javaee7.jpa.dynamicnamedquery;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.inject.Inject;

import org.javaee7.jpa.dynamicnamedquery.entity.TestEntity;
import org.javaee7.jpa.dynamicnamedquery.service.TestService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

/**
 * This tests that queries which have been dynamically (programmatically) added as named queries
 * can be executed correctly.
 * 
 * @author Arjan Tijms
 * 
 */
@RunWith(Arquillian.class)
public class DynamicNamedQueryTest {

    @Inject
    private TestService testService;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).addPackages(true, "org.javaee7.jpa.dynamicnamedquery")
            .addAsResource("META-INF/persistence.xml");
    }

    @Test
    public void testDynamicNamedCriteriaQueries() throws IOException, SAXException {

        // Nothing inserted yet, data base should not contain any entities 
        // (this tests that a simple query without parameters works as named query created by Criteria)
        assertTrue(testService.getAll().size() == 0);

        // Insert one entity
        TestEntity testEntity = new TestEntity();
        testEntity.setValue("myValue");
        testService.save(testEntity);

        // The total amount of entities should be 1
        assertTrue(testService.getAll().size() == 1);

        // The entity with "myValue" should be found
        // (this tests that a query with a parameter works as named query created by Criteria)
        assertTrue(testService.getByValue("myValue").size() == 1);
    }

}