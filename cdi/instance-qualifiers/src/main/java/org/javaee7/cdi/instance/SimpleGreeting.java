package org.javaee7.cdi.instance;

/**
 * @author Arun Gupta
 * @author Radim Hanus
 */
public class SimpleGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
}
