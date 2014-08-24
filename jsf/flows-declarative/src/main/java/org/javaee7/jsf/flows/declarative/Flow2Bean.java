package org.javaee7.jsf.flows.declarative;

import java.io.Serializable;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@FlowScoped("flow2")
public class Flow2Bean implements Serializable {

    public String getName() {
        return this.getClass().getSimpleName();
    }
}
