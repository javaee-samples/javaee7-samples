package org.javaee7.batch.chunk.checkpoint;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemProcessor implements ItemProcessor {

    @Override
    public MyOutputRecord processItem(Object t) {
        System.out.println("processItem: " + t);
        
        return (((MyInputRecord)t).getId() % 2 == 0) ? null : new MyOutputRecord(((MyInputRecord)t).getId() * 2);
    }
}
