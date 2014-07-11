package org.javaee7.batch.samples.scheduling;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;

/**
 * @author Roberto Cortez
 */
@Alternative
@Stateless
@Local(MyManagedScheduledBatch.class)
public class MyManagedScheduledBatchAlternative extends MyManagedScheduledBatchBean {
    @Override
    protected MyJob createJob() {
        return new MyJobAlternative();
    }
}
