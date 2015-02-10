package org.javaee7.ejb.embeddable;

import javax.ejb.embeddable.EJBContainer;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Arun Gupta
 */
public class MyBeanTest {

    /**
     * Test of sayHello method, of class MyBean.
     * 
     * Commented for now as support for this API is optional
     */
    //    @Test
    public void testSayHello() throws Exception {
        System.out.println("sayHello");
        String name = "Duke";
        try (EJBContainer container = javax.ejb.embeddable.EJBContainer.createEJBContainer()) {
            MyBean instance = (MyBean) container.getContext().lookup("java:global/classes/MyBean");
            String expResult = "Hello " + name;
            String result = instance.sayHello(name);
            assertEquals(expResult, result);
        }
    }
}
