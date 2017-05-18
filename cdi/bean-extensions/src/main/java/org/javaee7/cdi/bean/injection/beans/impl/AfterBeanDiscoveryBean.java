package org.javaee7.cdi.bean.injection.beans.impl;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Matt Gill
 */
@Dependent
public class AfterBeanDiscoveryBean {
    
    @PersistenceContext(unitName = "testUnit")
    private EntityManager em1;
    
    private EntityManager em2;
    
    @PersistenceContext(unitName = "testUnit")
    public void setEm2(EntityManager em) {
        this.em2 = em;
    }
    
    public boolean correctlyInjected() {
        if (em1 == null) return false;
        if (em2 == null) return false;
        return true;
    }
    
}
