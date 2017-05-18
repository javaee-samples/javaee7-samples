package org.javaee7.cdi.bean.injection.beans;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Matt Gill
 */
public abstract class TestBean {
    
    @PersistenceContext(unitName = "testUnit")
    private EntityManager em1;
    
    private EntityManager em2;
    
    @PersistenceContext(unitName = "testUnit")
    private void setEm2(EntityManager em) {
        this.em2 = em;
    }
    
    public boolean correctlyInjected() {
        if (em1 == null) return false;
        if (em2 == null) return false;
        return true;
    }
}
