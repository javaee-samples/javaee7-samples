package org.javaee7.jms.xa;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private JMSSender jmsSender;

    public User register(String email) {
        User user = new User(email);
        
        entityManager.persist(user);
        jmsSender.sendMessage("Hello " + email);
        
        return user;
    }
}
