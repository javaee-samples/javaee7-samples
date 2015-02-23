package org.javaee7.cdi.bean.discovery.none;

/**
 * @author Arun Gupta
 */
public class FancyGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name + ":)";
    }

}
