package org.javaee7.cdi.alternatives;

import javax.enterprise.inject.Alternative;


/**
 * @author Radim Hanus
 */
@Alternative
@Personal
public class AlternativePersonalGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Fine to see you, hi " + name;
    }

}
