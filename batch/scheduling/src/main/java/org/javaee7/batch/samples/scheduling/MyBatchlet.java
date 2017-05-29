package org.javaee7.batch.samples.scheduling;

import static javax.batch.runtime.BatchStatus.COMPLETED;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

/**
 * @author Roberto Cortez
 */
@Named
public class MyBatchlet extends AbstractBatchlet {
    
    @Override
    public String process() {
        System.out.println("Running inside a batchlet");

        return COMPLETED.toString();
    }
}
