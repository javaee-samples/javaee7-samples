package org.javaee7.jpa.aggregate_function_in_select;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import org.javaee7.jpa.aggregate_function_in_select.entity.AggregatedTestEntity;
import org.javaee7.jpa.aggregate_function_in_select.service.TestService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This tests that an aggregate function can be used in the select clause 
 * <p>
 * Currently the JPQL constructor expression is tested for this.
 * 
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class AggregateFunctionInSelectTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Inject
    private TestService testService;

    @Deployment
    public static Archive<?> deploy() {
        return ShrinkWrap.create(WebArchive.class)
            .addPackages(true, AggregateFunctionInSelectTest.class.getPackage())
            .addAsResource("META-INF/persistence.xml")
            .addAsWebInfResource(resource("web.xml"))
            .addAsLibraries(Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.hsqldb:hsqldb")
                .withoutTransitivity()
                .asSingleFile());
    }

    @Test
    public void aggregateFunctionInCtorSelectJPQL() throws Exception {

        testService.saveEntities();

        List<AggregatedTestEntity> testEntities = testService.getAggregation();
        
        assertTrue(
            "All entities should have been aggregated into 1 result row", 
            testEntities.size() == 1
        );
        
        String values = testEntities.get(0).getValues();
        
        assertTrue(
            "Aggregation should be 1,2 or 2,1, but was: " + values, 
            values.equals("1,2") || values.equals("2,1") // order doesn't matter here
        );
    }

    private static File resource(String name) {
        return new File(WEBAPP_SRC + "/WEB-INF", name);
    }
}
