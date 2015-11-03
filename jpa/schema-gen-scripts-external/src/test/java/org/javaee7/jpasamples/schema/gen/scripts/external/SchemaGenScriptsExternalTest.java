package org.javaee7.jpasamples.schema.gen.scripts.external;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

import static java.lang.Thread.currentThread;
import static org.apache.commons.io.FileUtils.copyURLToFile;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class SchemaGenScriptsExternalTest {
    @Inject
    private EmployeeBean employeeBean;

    @Deployment
    public static WebArchive createDeployment() throws Exception {
        copyURLToFile(currentThread().getContextClassLoader().getResource("META-INF/create.sql"),
            new File("/tmp/create.sql"));
        copyURLToFile(currentThread().getContextClassLoader().getResource("META-INF/drop.sql"),
            new File("/tmp/drop.sql"));
        copyURLToFile(currentThread().getContextClassLoader().getResource("META-INF/load.sql"),
            new File("/tmp/load.sql"));

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpasamples.schema.gen.scripts.external")
            .addAsResource("META-INF/persistence.xml")
            .addAsResource("META-INF/create.sql")
            .addAsResource("META-INF/drop.sql")
            .addAsResource("META-INF/load.sql");
        System.out.println(war.toString(true));
        return war;
    }

    @After
    public void tearDown() throws Exception {
        new File("/tmp/create.sql").delete();
        new File("/tmp/drop.sql").delete();
        new File("/tmp/load.sql").delete();
    }

    @Test
    public void testSchemaGenScriptExternal() throws Exception {
        Assert.assertFalse(employeeBean.get().isEmpty());
    }
}
