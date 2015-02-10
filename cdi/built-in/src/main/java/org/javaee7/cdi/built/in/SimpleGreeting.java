package org.javaee7.cdi.built.in;

import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author Arun Gupta
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class SimpleGreeting implements Greeting {

    @Inject
    HttpServletRequest httpServletRequest;

    @Inject
    HttpSession httpSession;

    @Inject
    ServletContext servletContext;

    @Inject
    UserTransaction ut;

    @Inject
    Principal principal;

    @Override
    public String greet(String name) {
        try {
            System.out.println("context path (HttpServletRequest): " + httpServletRequest.getContextPath());
            System.out.println("session id: " + httpSession.getId());
            System.out.println("context path (ServletContext): " + servletContext.getContextPath());
            System.out.println("user transaction status: " + ut.getStatus());
            System.out.println("security principal: " + principal.getName());
        } catch (SystemException ex) {
            Logger.getLogger(SimpleGreeting.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Hello " + name;
    }

}
