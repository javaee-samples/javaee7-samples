package org.javaee7.jsf.viewscoped;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

/**
 * @author Arun Gupta
 */
@ManagedBean
@ViewScoped
public class MyBean implements Serializable {
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
