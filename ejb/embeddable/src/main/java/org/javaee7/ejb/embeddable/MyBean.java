package org.javaee7.ejb.embeddable;

import javax.ejb.Stateless;

/**
 * @author Arun Gupta
 */
@Stateless
public class MyBean {

    public String sayHello(String name) {
        return "Hello " + name;
    }

}
