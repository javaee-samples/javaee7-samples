package org.javaee7.cdi.alternatives;


/**
 * @author Radim Hanus
 */
@Personal
public class PersonalGreeting implements Greeting {
    @Override
    public String greet(String name) {
        return "Hi " + name;
    }
}
