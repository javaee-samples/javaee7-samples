package org.javaee7.jacc.contexts.bean;

import javax.ejb.Stateless;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Arjan Tijms
 *
 */
@Stateless
public class JaccRequestBean {

    public HttpServletRequest getRequest() throws PolicyContextException {
        return (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
    }

    public boolean hasAttribute() throws PolicyContextException {
        return "true".equals(getRequest().getAttribute("jaccTest"));
    }

    public boolean hasParameter() throws PolicyContextException {
        return "true".equals(getRequest().getParameter("jacc_test"));
    }
}
