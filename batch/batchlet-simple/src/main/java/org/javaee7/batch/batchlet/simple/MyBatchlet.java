package org.javaee7.batch.batchlet.simple;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyBatchlet extends AbstractBatchlet {
    @Override
    public String process() {
        System.out.println("Running inside a batchlet");

        return BatchStatus.COMPLETED.toString();
    }
}
