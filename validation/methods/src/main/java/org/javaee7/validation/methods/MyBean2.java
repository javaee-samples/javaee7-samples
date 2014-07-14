package org.javaee7.validation.methods;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author Arun Gupta
 */
@RequestScoped
public class MyBean2 {
    private MyParameter value;

    public MyBean2() {

    }

    @Inject
    public MyBean2(@Valid MyParameter value) {
        this.value = value;
    }


    public MyParameter getValue() {
        return value;
    }
}
