package org.javaee7.cdi.specializes;

import javax.enterprise.inject.Specializes;


/**
 * @author Radim Hanus
 */
@Specializes
public class SpecializedGreeting extends Greeting {
    @Override
    public String greet(String name) {
        return "Hello my friend " + name;
    }
}
