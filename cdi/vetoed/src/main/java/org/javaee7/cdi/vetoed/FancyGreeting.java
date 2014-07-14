package org.javaee7.cdi.vetoed;

/**
 * @author Arun Gupta
 */
public class FancyGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name + " :)";
    }
    
}
