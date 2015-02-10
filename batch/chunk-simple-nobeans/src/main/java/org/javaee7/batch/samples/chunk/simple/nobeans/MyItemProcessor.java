package org.javaee7.batch.samples.chunk.simple.nobeans;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Dependent
@Named
public class MyItemProcessor implements ItemProcessor {

    @Override
    public MyOutputRecord processItem(Object t) {
        System.out.println("processItem: " + t);

        return (((MyInputRecord) t).getId() % 2 == 0) ? null : new MyOutputRecord(((MyInputRecord) t).getId() * 2);
    }
}
