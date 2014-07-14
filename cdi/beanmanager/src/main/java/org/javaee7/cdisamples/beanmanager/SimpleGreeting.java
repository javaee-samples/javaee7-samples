package org.javaee7.cdisamples.beanmanager;

/**
 * @author Arun Gupta
 */
public class SimpleGreeting implements Greeting {

    @Override
    public String greet(String name) {
        return "Hello " + name;
    }
    
}
