package org.javaee7.jaspic.invoke.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class CDIBean {

    public String getText() {
        return "Called from CDI";
    }
    
}
