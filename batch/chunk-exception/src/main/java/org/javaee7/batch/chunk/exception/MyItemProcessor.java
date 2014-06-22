package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyItemProcessor implements ItemProcessor {

    @Override
    public Object processItem(Object t) {
        System.out.println("MyItemProcessor.processItem: " + t);

        if (((MyInputRecord) t).getId() == 6) {
            throw new NullPointerException();
        }

        return new MyOutputRecord(((MyInputRecord) t).getId());
    }
}
