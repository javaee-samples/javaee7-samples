package org.javaee7.cdi.alternatives;

import javax.enterprise.inject.Alternative;

/**
 * @author Arun Gupta
 */
@Alternative
public class FancyGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Nice to meet you, hello" + name;
    }

}
