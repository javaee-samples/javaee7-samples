package org.javaee7.cdi.dynamic.interceptor;

import org.javaee7.cdi.dynamic.interceptor.extension.Hello;

/**
 * 
 * @author Arjan Tijms
 *
 */
public class MyBean {
    
    @Hello
    public String getName() {
        return "John";
    }
}
