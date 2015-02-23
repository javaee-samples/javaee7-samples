package org.javaee7.jpa.nativesql.resultset.mapping;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * In this sample we're going to query a simple +JPA Entity+, using the +JPA EntityManager Native Query+, perform
 * a select operation and map the query result using +@SqlResultSetMapping+.
 *
 * include::Employee[]
 *
 * The select operation is very simple. We just need to call the API method +createNativeQuery+ on the +EntityManager+
 * and use the mapping defined on +Employee+ by the +@SqlResultSetMapping+ annotation.
 *
 * include::EmployeeBean#get[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class JpaNativeSqlResultSetMappingTest {
    @Inject
    private EmployeeBean employeeBean;

    /**
     * We're just going to deploy the application as a +web archive+. Note the inclusion of the following files:
     *
     * [source,file]
     * ----
     * /META-INF/persistence.xml
     * /META-INF/create.sql
     * /META-INF/drop.sql
     * /META-INF/load.sql
     * ----
     *
     * The +persistence.xml+ file is needed of course for the persistence unit definition. A datasource is not
     * needed, since we can now use the new default datasource available in +JEE7+. We're also using the new
     * +javax.persistence.schema-generation.*+ propertires to create, populate and drop the database.
     */
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.nativesql.resultset.mapping")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/drop.sql")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * In the test, we're just going to invoke the only available operation in the +EmployeeBean+ and assert a few
     * details to confirm that the native query was successfully executed.
     */
    @Test
    public void testJpaNativeSqlResultSetMapping() {
        List<Employee> employees = employeeBean.get();
        assertFalse(employees.isEmpty());
        assertEquals(8, employees.size());
    }
}
