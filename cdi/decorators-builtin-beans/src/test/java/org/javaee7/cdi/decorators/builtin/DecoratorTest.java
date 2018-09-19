/** Copyright Payara Services Limited **/
package org.javaee7.cdi.decorators.builtin;

import static org.hamcrest.core.Is.is;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.javaee7.cdi.decorators.builtin.RequestDecorator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class DecoratorTest {

    @Inject
    private HttpServletRequest request;

    @Deployment
    public static Archive<?> deploy() {
        return create(JavaArchive.class)
            .addAsManifestResource(new File("src/main/webapp/WEB-INF/beans.xml"), "beans.xml")
            .addPackage(RequestDecorator.class.getPackage());
    }

    @Test
    public void test() {
        assertThat(request.getParameter("decorated"), is("true"));
    }
}
