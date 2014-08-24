package org.javaee7.batch.multiple.steps;

import javax.batch.api.AbstractBatchlet;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyBatchlet extends AbstractBatchlet {

    @Override
    public String process() {
        System.out.println("Running inside a batchlet");
        
        return "COMPLETED";
    }

}
