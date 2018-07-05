package org.javaee7.cdi.decorators.priority;

import static org.hamcrest.core.Is.is;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URISyntaxException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Korneliusz Rabczak
 */
@RunWith(Arquillian.class)
public class DecoratorTest {

    @Inject
    private Greeting greeting;

    @Deployment
    public static Archive<?> deploy() throws URISyntaxException {
        return create(JavaArchive.class)
            .addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
            .addPackage(SimpleGreeting.class.getPackage());
    }

    @Test
    public void test() {
        assertThat(greeting.greet("Duke"), is("Hello Duke very much!"));
    }
}
