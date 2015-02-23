package org.javaee7.cdi.bean.discovery.annotated;

import javax.enterprise.context.RequestScoped;

/**
 * @author Arun Gupta
 */
@RequestScoped
public class SimpleGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name;
    }

}
