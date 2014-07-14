package org.javaee7.jpa.jndi.context;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@PersistenceContext(name = "persistence/myJNDI", unitName = "MyPU")
@Stateless
public class EmployeeBean {

    EntityManager em;
    
    @PostConstruct
    public void postConstruct() {
        try {
            Context context = new InitialContext();
            em = (EntityManager) context.lookup("java:comp/env/persistence/myJNDI");
        } catch (NamingException ex) {
            Logger.getLogger(EmployeeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<Employee> get() {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }
}
