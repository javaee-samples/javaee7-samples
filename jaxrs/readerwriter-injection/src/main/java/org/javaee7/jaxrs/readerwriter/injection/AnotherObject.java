/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
