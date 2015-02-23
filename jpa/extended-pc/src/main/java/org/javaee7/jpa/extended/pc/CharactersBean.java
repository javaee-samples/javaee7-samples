package org.javaee7.jpa.extended.pc;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;
import java.util.List;

/**
 * @author Kuba Marchwicki
 */
@Stateful
@TransactionAttribute(TransactionAttributeType.NEVER)
public class CharactersBean implements Serializable {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager em;

    public void save(Character e) {
        em.persist(e);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void commitChanges() {

    }

    public List<Character> get() {
        return em.createNamedQuery(Character.FIND_ALL, Character.class).getResultList();
    }

}
