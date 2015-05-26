package org.javaee7.cdi.instance;

/**
 * @author Radim Hanus
 */
@Business
public class FormalGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Good morning " + name;
    }
}
