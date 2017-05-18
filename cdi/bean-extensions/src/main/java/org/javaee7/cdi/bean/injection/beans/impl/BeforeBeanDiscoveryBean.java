package org.javaee7.cdi.bean.injection.beans.impl;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.javaee7.cdi.bean.injection.beans.TestBean;

/**
 * @author Matt Gill
 */
@Dependent
public class BeforeBeanDiscoveryBean implements TestBean{
    
    @PersistenceContext(unitName = "testUnit")
    private EntityManager em1;
    
    private EntityManager em2;
    
    @PersistenceContext(unitName = "testUnit")
    private void setEm2(EntityManager em) {
        this.em2 = em;
    }
    
    public EntityManager getEm1() {
        return em1;
    }
    
    public EntityManager getEm2() {
        return em2;
    }
    
    @Override
    public boolean correctlyInjected() {
        if (em1 == null) return false;
        if (em2 == null) return false;
        return true;
    }
}
