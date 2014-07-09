package org.javaee7.batch.samples.scheduling;

import org.javaee7.util.BatchTestHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class TimerScheduleBatchTest {
    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(BatchTestHelper.class)
                .addClass(MyBatchlet.class)
                .addClass(MyJob.class)
                .addClass(MySingletonEJB.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/batch-jobs/myJob.xml");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testTimeScheduleBatch() throws Exception {
        MySingletonEJB.timerScheduleCountDownLatch.await(2, TimeUnit.MINUTES);

        assertEquals(3, MySingletonEJB.executedBatchs.size());

        for (Long executedBatch : MySingletonEJB.executedBatchs) {
            assertEquals(BatchStatus.COMPLETED,
                    BatchRuntime.getJobOperator().getJobExecution(executedBatch).getBatchStatus());
        }
    }
}
