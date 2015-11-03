package org.javaee7.jaspic.dispatching.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class MyBean {

    public String getText() {
        return "Called from CDI";
    }
    
}
