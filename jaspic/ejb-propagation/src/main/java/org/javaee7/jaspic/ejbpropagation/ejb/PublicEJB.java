package org.javaee7.jaspic.ejbpropagation.ejb;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;

/**
 * This is a "public" EJB in the sense that all its methods should be accessible and there is no declarative role checking prior
 * to accessing a method.
 * 
 * @author Arjan Tijms
 * 
 */
@Stateless
public class PublicEJB {

    @Resource
    private EJBContext ejbContext;

    public String getUserName() {
        try {
            return ejbContext.getCallerPrincipal() != null ? ejbContext.getCallerPrincipal().getName() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
