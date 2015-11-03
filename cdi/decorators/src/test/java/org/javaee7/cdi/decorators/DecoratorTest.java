package org.javaee7.cdi.decorators;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Korneliusz Rabczak
 */
@RunWith(Arquillian.class)
public class DecoratorTest {

    @Inject
    Greeting greeting;

    @Deployment
    public static Archive<?> deploy() throws URISyntaxException {
        return ShrinkWrap.create(JavaArchive.class)
            .addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
            .addPackage(SimpleGreeting.class.getPackage());
    }

    @Test
    public void test() {
        assertThat(greeting.greet("Duke"), is("Hello Duke very much!"));
    }
}
