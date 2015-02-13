package org.javaee7.jpa.datasourcedefinition;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.sql.DataSource;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Alexis Hassler
 */
@RunWith(Arquillian.class)
public class DataSourceDefinitionTest {
    @Deployment
    public static Archive<?> deploy() {
        File h2Library = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("com.h2database:h2").withoutTransitivity()
            .asSingleFile();

        return ShrinkWrap.create(WebArchive.class)
            .addClasses(DataSourceDefinitionHolder.class)
            .addAsLibraries(h2Library);
    }

    @Resource(lookup = "java:global/MyApp/MyDataSource")
    DataSource dataSource;

    @Test
    public void should_bean_be_injected() throws Exception {
        assertThat(dataSource, is(notNullValue()));
        assertThat(dataSource.getConnection(), is(notNullValue()));
    }
}
