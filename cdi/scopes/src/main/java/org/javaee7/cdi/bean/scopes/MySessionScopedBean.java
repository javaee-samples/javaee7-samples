package org.javaee7.cdi.bean.scopes;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;

/**
 * @author Arun Gupta
 * 
 * This class represents a Session Scoped CDI bean. Once injected, the container will hold on to the instance of this bean until
 * the HTTP session expires. A new instance would be created with start of a fresh HTTP session
 */
@SessionScoped
public class MySessionScopedBean implements Serializable{
    public String getID() {
        return this + "";
    }
}
