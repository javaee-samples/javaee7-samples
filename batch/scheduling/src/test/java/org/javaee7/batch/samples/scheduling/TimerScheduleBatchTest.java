package org.javaee7.batch.samples.scheduling;

import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.batch.runtime.BatchRuntime.getJobOperator;
import static javax.batch.runtime.BatchStatus.COMPLETED;
import static org.javaee7.batch.samples.scheduling.MyStepListener.countDownLatch;
import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * The Batch specification does not offer anything to schedule jobs. However, the Java EE plataform offer a few ways
 * that allow you to schedule Batch jobs.
 *
 * Annotating a method bean with +javax.ejb.Schedule+, it's possible to schedule an execution of a batch job by the
 * specified cron expression in the +javax.ejb.Schedule+ annotation.
 *
 * include::AbstractTimerBatch[]
 *
 * include::MyTimerScheduleBean[]
 *
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class TimerScheduleBatchTest {
    
    /**
     * We're just going to deploy the application as a +web archive+. Note the inclusion of the following files:
     *
     * [source,file]
     * ----
     * /META-INF/batch-jobs/myJob.xml
     * ----
     *
     * The +myJob.xml+ file is needed for running the batch definition. We are also adding an alternative bean to
     * override the batch schedule timeout and track the execution calls,
     *
     * include::MyTimerScheduleAlternative[]
     */
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addClasses(
                MyJob.class,
                MyBatchlet.class,
                MyStepListener.class,
                AbstractTimerBatch.class,
                MyTimerScheduleAlternative.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
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
        countDownLatch.await(90, SECONDS);

        assertEquals(0, countDownLatch.getCount());
        assertEquals(3, MyTimerScheduleAlternative.executedBatchs.size());

        for (Long executedBatch : MyTimerScheduleAlternative.executedBatchs) {
            
            System.out.println("TimerScheduleBatchTest checking completed for batch " + executedBatch);
            
            assertEquals(
                COMPLETED,
                getJobOperator().getJobExecution(executedBatch).getBatchStatus());
        }
    }
}
