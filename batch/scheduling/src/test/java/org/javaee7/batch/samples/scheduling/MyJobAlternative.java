package org.javaee7.batch.samples.scheduling;

/**
 * @author Roberto Cortez
 */
public class MyJobAlternative extends MyJob {

    @Override
    protected void afterRun() {
        System.out.println("Job submitted");
    }
}
