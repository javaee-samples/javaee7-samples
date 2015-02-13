package org.javaee7.batch.decision;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyBatchlet2 extends AbstractBatchlet {

    @Override
    public String process() {
        System.out.println("Running inside a batchlet 2");

        return "COMPLETED";
    }

}
