package org.javaee7.cdi.pkg.level.beans;

import org.javaee7.cdi.pkg.level.Greeting;

/**
 * @author Arun Gupta
 */
public class SimpleGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name;
    }

}
