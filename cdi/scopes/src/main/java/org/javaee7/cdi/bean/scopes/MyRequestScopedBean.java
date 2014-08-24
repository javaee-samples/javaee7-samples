package org.javaee7.cdi.bean.scopes;

import javax.enterprise.context.RequestScoped;

/**
 * @author Arun Gupta
 * 
 * This class represents a Request Scoped CDI bean. The container will create a new instance of this bean for every single HTTP request.
 */
@RequestScoped
public class MyRequestScopedBean {
    public String getID() {
        return this + "";
    }
}
