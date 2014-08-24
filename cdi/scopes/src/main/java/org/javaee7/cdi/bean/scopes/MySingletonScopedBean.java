package org.javaee7.cdi.bean.scopes;

import javax.inject.Singleton;

/**
 * @author Arun Gupta
 */
@Singleton
public class MySingletonScopedBean {
    public String getID() {
        return this + "";
    }
}
