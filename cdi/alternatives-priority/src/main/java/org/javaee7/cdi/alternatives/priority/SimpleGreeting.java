package org.javaee7.cdi.alternatives.priority;

import javax.enterprise.inject.Alternative;

/**
 * @author Arun Gupta
 * @author Radim Hanus
 */
@Alternative
public class SimpleGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
}
