package org.javaee7.jaspic.dispatching.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class MyBean {
    
    @Inject
    private HttpServletRequest request;

    public String getText() {
        return "Called from CDI\n";
    }
    
    public String getServletPath() {
        return request.getServletPath();
    }
    
}
