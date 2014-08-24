package org.javaee7.jpa.pu.typesafe;

import javax.annotation.ManagedBean;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@ManagedBean
public class ProducerBean {

    static @Produces @PersistenceContext(unitName = "defaultPU") @DefaultDatabase EntityManager defaultEM;

}
