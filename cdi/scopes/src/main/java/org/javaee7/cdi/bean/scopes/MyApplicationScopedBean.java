package org.javaee7.cdi.bean.scopes;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author Arun Gupta
 * 
 * This class represents an Application Scoped CDI bean. Once injected, the container will hold on to the instance of this bean until
 * the application itself terminates (server restart/redeployment of the application).
 */
@ApplicationScoped
public class MyApplicationScopedBean {
    public String getID() {
        return this + "";
    }
}
