package org.javaee7.batch.samples.scheduling;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * The Batch specification does not offer anything to schedule jobs. However, the Java EE plataform offer a few ways
 * that allow you to schedule Batch jobs.
 *
 * Adding a +javax.enterprise.concurrent.Trigger+ to a +javax.enterprise.concurrent.ManagedScheduledExecutorService+
 * is possible to trigger an execution of the batch job by specifying the next execution date of the job.
 *
 * include::MyManagedScheduledBatchBean[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class ManagedScheduledBatchTest {
    @Inject
    private MyManagedScheduledBatch managedScheduledBatch;

    /**
     * We're just going to deploy the application as a +web archive+. Note the inclusion of the following files:
     *
     * [source,file]
     * ----
     * /META-INF/batch-jobs/myJob.xml
     * ----
     *
     * The +myJob.xml+ file is needed for running the batch definition. We are also adding an alternative bean to
     * override the created batch instance do we can track it's status and the modified batch instance.
     *
     * include::MyJobAlternative[]
     *
     * include::MyManagedScheduledBatchAlternative[]
     */
    @Deployment
    public static WebArchive createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyBatchlet.class,
                MyJob.class,
                MyJobAlternative.class,
                MyManagedScheduledBatch.class,
                MyManagedScheduledBatchBean.class,
                MyManagedScheduledBatchAlternative.class)
            .addAsWebInfResource(
                new StringAsset(beansXml.createAlternatives().clazz(
                    MyManagedScheduledBatchAlternative.class.getName()).up().exportAsString()),
                beansXml.getDescriptorName())
            .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * The batch job is scheduled to execute each 15 seconds. We expect to run the batch instance exactly 3 times as
     * defined in the +CountDownLatch+ object. To validate the test expected behaviour we just need to check the
     * Batch Status in the +javax.batch.runtime.JobExecution+ object. We should get a
     * +javax.batch.runtime.BatchStatus.COMPLETED+ for every execution.
     *
     * @throws Exception an exception if the batch could not complete successfully.
     */
    @Test
    public void testTimeScheduleBatch() throws Exception {
        managedScheduledBatch.runJob();

        MyJobAlternative.managedScheduledCountDownLatch.await(90, TimeUnit.SECONDS);

        assertEquals(0, MyJobAlternative.managedScheduledCountDownLatch.getCount());
        assertEquals(3, MyJob.executedBatchs.size());

        for (Long executedBatch : MyJob.executedBatchs) {
            assertEquals(BatchStatus.COMPLETED,
                BatchRuntime.getJobOperator().getJobExecution(executedBatch).getBatchStatus());
        }
    }
}
