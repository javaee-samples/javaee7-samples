package org.javaee7.jms.xa;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Stateless
public class UserManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private Mailman mailman;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public User register(String email)
    {
        final User user = new User(email);
        entityManager.persist(user);
        mailman.sendMessage("Hello " + email);
        return user;
    }
}
