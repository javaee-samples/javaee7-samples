package org.javaee7.jpasamples.schema.gen.scripts.generate;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class SchemaGenScriptsTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = create(WebArchive.class)
                .addPackage("org.javaee7.jpasamples.schema.gen.scripts.generate")
                .addAsResource("META-INF/persistence.xml");
        
        System.out.println(war.toString(true));
        
        return war;
    }

    @After
    public void tearDown() throws Exception {
        System.out.println("Absolute path of create-script.sql: " + new File("target/create-script.sql").getAbsolutePath());
        new File("target/create-script.sql").delete();
        new File("target/drop-script.sql").delete();
    }

    @Test
    @RunAsClient
    public void testSchemaGenIndex() throws Exception {
        Path create = get("target","create-script.sql");
        Path drop = get("target","drop-script.sql");
        
        assertTrue(exists(create));
        assertTrue(exists(drop));

        assertTrue(new String(readAllBytes(create), UTF_8).toLowerCase().contains("create table employee_schema_gen_scripts_generate"));
        assertTrue(new String(readAllBytes(drop), UTF_8).toLowerCase().contains("drop table employee_schema_gen_scripts_generate"));
    }
}
