package org.javaee7.jpa.index;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class SchemaGenIndexTest {
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addPackage("org.javaee7.jpa.index")
            .addAsResource("META-INF/persistence.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testSchemaGenIndex() throws Exception {
        Path create = Paths.get("/tmp/create.sql");
        assertTrue(Files.exists(create));

        Path drop = Paths.get("/tmp/drop.sql");
        assertTrue(Files.exists(create));

        String line;
        BufferedReader reader = Files.newBufferedReader(create, Charset.defaultCharset());
        boolean createGenerated = false;
        boolean createIndex = false;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("create table employee_schema_gen_index")) {
                createGenerated = true;
            }

            if (line.toLowerCase().contains("create index")) {
                createIndex = true;
            }
        }

        reader = Files.newBufferedReader(drop, Charset.defaultCharset());
        boolean dropGenerated = false;
        while ((line = reader.readLine()) != null) {
            if (line.toLowerCase().contains("drop table employee_schema_gen_index")) {
                dropGenerated = true;
                break;
            }
        }

        assertTrue(createGenerated);
        assertTrue(createIndex);
        assertTrue(dropGenerated);
    }
}
