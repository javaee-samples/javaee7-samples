package org.javaee7.batch.chunk.csv.database;

import java.util.List;
import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemWriter extends AbstractItemWriter {
    
    @PersistenceContext
    EntityManager em;

    @Override
    public void writeItems(List list) {
        System.out.println("writeItems: " + list);
        for (Object person : list) {
            em.persist(person);
        }
    }
}
