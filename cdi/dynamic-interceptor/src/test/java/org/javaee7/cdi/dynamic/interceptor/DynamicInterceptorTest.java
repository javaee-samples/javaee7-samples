package org.javaee7.cdi.dynamic.interceptor;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.javaee7.cdi.dynamic.interceptor.MyBean;
import org.javaee7.cdi.dynamic.interceptor.extension.CdiExtension;
import org.javaee7.cdi.dynamic.interceptor.extension.DynamicHelloInterceptor;
import org.javaee7.cdi.dynamic.interceptor.extension.DynamicInterceptorBase;
import org.javaee7.cdi.dynamic.interceptor.extension.Hello;
import org.javaee7.cdi.dynamic.interceptor.extension.HelloInterceptorEnabler;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class DynamicInterceptorTest {
    
    @Deployment
    public static WebArchive deploy() {
        WebArchive war = create(WebArchive.class)
                .addClasses(MyBean.class)
                .addAsLibraries(
                    create(JavaArchive.class)
                        .addClasses(CdiExtension.class, DynamicHelloInterceptor.class, DynamicInterceptorBase.class, Hello.class, HelloInterceptorEnabler.class)
                        .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension"))
                .addAsWebInfResource("beans.xml");
        
        System.out.println(war.toString(true));
        
        return war;
    }

    @Inject
    private MyBean myBean;

    @Test
    public void test() {
        assertEquals("Hello, John", myBean.getName());
    }
}
