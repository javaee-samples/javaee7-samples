package org.javaee7.cdi.beanmanager;

/**
 * @author Arun Gupta
 */
public class SmileyGreeting extends SimpleGreeting {

    @Override
    public String greet(String name) {
        return super.greet(name) + " :-)";
    }

}
