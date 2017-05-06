package org.javaee7.jpasamples.schema.gen.scripts.generate;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
@Startup
public class StartupBean {
    
    // Some provider like EclipseLink on Payara initialize JPA more lazily
    // and need this to be able to spring into action and generate the scripts
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        System.out.println("Hello, world");
    }
    
}
