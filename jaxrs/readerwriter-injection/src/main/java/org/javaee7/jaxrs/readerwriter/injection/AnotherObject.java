package org.javaee7.jaxrs.readerwriter.injection;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Arun Gupta
 */
@ApplicationScoped
public class AnotherObject {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
