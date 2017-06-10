package org.javaee7.batch.samples.scheduling;

import static com.jayway.awaitility.Awaitility.await;
import static com.jayway.awaitility.Duration.FIVE_HUNDRED_MILLISECONDS;
import static com.jayway.awaitility.Duration.ONE_MINUTE;
import static java.lang.System.out;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static javax.batch.runtime.BatchStatus.STARTED;
import static org.javaee7.Libraries.awaitability;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;

import javax.batch.runtime.JobExecution;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

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
     * override the created batch instance so we can track its status and the modified batch instance.
     *
     * include::MyJobAlternative[]
     *
     * include::MyManagedScheduledBatchAlternative[]
     */
    @Deployment
    public static WebArchive createDeployment() {
        BeansDescriptor beansXml = Descriptors.create(BeansDescriptor.class);

        WebArchive war = create(WebArchive.class)
            .addClasses(
                MyBatchlet.class,
                MyJob.class,
                MyStepListener.class,
                MyJobAlternative.class,
                MyManagedScheduledBatch.class,
                MyManagedScheduledBatchBean.class,
                MyManagedScheduledBatchAlternative.class)
            .addAsWebInfResource(
                new StringAsset(beansXml.getOrCreateAlternatives().clazz(
                    MyManagedScheduledBatchAlternative.class.getName()).up().exportAsString()),
                beansXml.getDescriptorName())
            .addAsResource("META-INF/batch-jobs/myJob.xml")
            .addAsLibraries(awaitability());
        
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

        MyStepListener.countDownLatch.await(90, SECONDS);

        // If this assert fails it means we've timed out above
        assertEquals(0, MyStepListener.countDownLatch.getCount());
        assertEquals(3, MyJob.executedBatchs.size());
        
        sleep(1000l);
        
        final JobExecution lastExecution = getJobOperator().getJobExecution(MyJob.executedBatchs.get(2));
        
        await().atMost(ONE_MINUTE)
               .with().pollInterval(FIVE_HUNDRED_MILLISECONDS)
               .until(                                                                                                                                                                                      new Callable<Boolean>() { @Override public Boolean call() throws Exception {
                    return lastExecution.getBatchStatus() != STARTED;                                                                                                                                        }}
                );

        for (Long executedBatch : MyJob.executedBatchs) {
            
            out.println("ManagedScheduledBatchTest checking completed for batch " + executedBatch);
            
            assertEquals(
                "Outcome equal for batch " + executedBatch,
                COMPLETED,
                getJobOperator().getJobExecution(executedBatch).getBatchStatus());
        }
    }
}
