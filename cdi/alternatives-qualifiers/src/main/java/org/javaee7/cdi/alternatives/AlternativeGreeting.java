package org.javaee7.cdi.alternatives;

import javax.enterprise.inject.Alternative;


/**
 * @author Radim Hanus
 */
@Alternative
public class AlternativeGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Nice to meet you, hello " + name;
    }

}
