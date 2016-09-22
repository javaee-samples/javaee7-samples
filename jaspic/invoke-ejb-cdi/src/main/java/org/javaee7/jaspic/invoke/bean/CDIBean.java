package org.javaee7.jaspic.invoke.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class CDIBean {
    
    @Inject
    private HttpServletRequest request;

    public String getText() {
        return "Called from CDI";
    }
    
    public void setTextViaInjectedRequest() {
        request.setAttribute("text", "Called from CDI via injected request");
    }
    
}
