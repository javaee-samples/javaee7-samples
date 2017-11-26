package org.javaee7.cdi.bean.injection.beans;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.javaee7.cdi.bean.injection.beans.types.CdiBean;
import org.javaee7.cdi.bean.injection.beans.types.EjbBean;

/**
 * @author Matt Gill
 */
public abstract class TestBean {
    
    @PersistenceContext(unitName = "testUnit")
    private EntityManager em1;
    
    private EntityManager em2;
    
    @Inject
    private CdiBean cdi1;
    
    private CdiBean cdi2;
    
    @EJB
    private EjbBean ejb1;
    
    private EjbBean ejb2;
    
    @Inject
    private EjbBean ejb3;
    
    private EjbBean ejb4;
    
    @PersistenceContext(unitName = "testUnit2")
    private void setEm2(EntityManager em) {
        this.em2 = em;
    }
    
    @Inject
    private void setCdi2(CdiBean cdi) {
        this.cdi2 = cdi;
    }
    
    @Inject
    private void setEjb2(EjbBean ejb) {
        this.ejb2 = ejb;
    }
    
    @EJB
    private void setEjb4(EjbBean ejb) {
        this.ejb4 = ejb;
    }
    
    public boolean correctlyInjected() {
        if (em1 == null) return false;
        if (em2 == null) return false;
        if (cdi1 == null) return false;
        if (cdi2 == null) return false;
        if (ejb1 == null) return false;
        if (ejb2 == null) return false;
        if (ejb3 == null) return false;
        if (ejb4 == null) return false;
        return true;
    }
}
