package org.javaee7.cdi.nobeans.el.injection.flowscoped;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@RequestScoped
public class FlowScopedBean {
    public String sayHello() {
        return "Hello there!";
    }
}
