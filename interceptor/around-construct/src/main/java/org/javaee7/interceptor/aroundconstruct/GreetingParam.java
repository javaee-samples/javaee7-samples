package org.javaee7.interceptor.aroundconstruct;

/**
 * @author Radim Hanus
 */
public class GreetingParam implements Param {
    private String value;

    public GreetingParam() {
        value = "Greeting";
    }

    @Override
    public String getValue() {
        return value;
    }
}
