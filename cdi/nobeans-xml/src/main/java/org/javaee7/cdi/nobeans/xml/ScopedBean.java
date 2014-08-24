package org.javaee7.cdi.nobeans.xml;

import javax.enterprise.context.RequestScoped;

/**
 * @author Arun Gupta
 */
@RequestScoped
public class ScopedBean {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
