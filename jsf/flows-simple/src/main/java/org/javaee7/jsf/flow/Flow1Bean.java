package org.javaee7.jsf.flow;

import java.io.Serializable;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@FlowScoped("flow1")
public class Flow1Bean implements Serializable {

    public Flow1Bean() {
        System.out.println("Flow1Bean.ctor");
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    public String getHomeAction() {
        return "/index";
    }
}
