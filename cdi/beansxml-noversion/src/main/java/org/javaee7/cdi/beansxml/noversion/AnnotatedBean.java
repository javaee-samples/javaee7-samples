package org.javaee7.cdi.beansxml.noversion;

import javax.enterprise.context.RequestScoped;

/**
 * @author Alexis Hassler
 */
@RequestScoped
public class AnnotatedBean {
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
