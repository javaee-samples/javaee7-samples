package org.javaee7.cdi.specializes;

import javax.inject.Named;


/**
 * @author Radim Hanus
 */
@Personal
@Named("base")
public class Greeting {
    public String greet(String name) {
        return "Hello " + name;
    }
}
