package org.javaee7.cdi.exclude.filter.beans;

import org.javaee7.cdi.exclude.filter.Greeting;

/**
 * @author Arun Gupta
 */
public class SimpleGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
    
}
