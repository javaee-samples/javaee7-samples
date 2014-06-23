package org.javaee7.batch.batch.listeners;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object t) {
        System.out.println("processItem: " + t);
        
        return (((MyInputRecord)t).getId() % 2 == 0) ? null : new MyOutputRecord(((MyInputRecord)t).getId() * 2);
    }
}
