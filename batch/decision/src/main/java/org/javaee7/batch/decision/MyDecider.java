package org.javaee7.batch.decision;

import javax.batch.api.Decider;
import javax.batch.runtime.StepExecution;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyDecider implements Decider {

    @Override
    public String decide(StepExecution[] ses) throws Exception {
        for (StepExecution se : ses) {
            System.out.println(se.getStepName() + " " + se.getBatchStatus().toString() + " " + se.getExitStatus());
        }
        return "foobar";
    }

}
