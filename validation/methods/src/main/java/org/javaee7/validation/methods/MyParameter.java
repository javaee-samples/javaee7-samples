package org.javaee7.validation.methods;

import javax.validation.constraints.NotNull;

public class MyParameter {

    @NotNull
    public String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
