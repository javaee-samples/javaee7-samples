package org.javaee7.cdi.alternatives;


/**
 * @author Radim Hanus
 */
public class DefaultGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
}
