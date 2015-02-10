package org.javaee7.cdi.alternatives;

import javax.enterprise.inject.Alternative;

/**
 * @author Arun Gupta
 */
@Alternative
public class SimpleGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name;
    }

}
