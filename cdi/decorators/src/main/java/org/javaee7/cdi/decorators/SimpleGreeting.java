package org.javaee7.cdi.decorators;

/**
 * @author Arun Gupta
 */
public class SimpleGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name;
    }

}
