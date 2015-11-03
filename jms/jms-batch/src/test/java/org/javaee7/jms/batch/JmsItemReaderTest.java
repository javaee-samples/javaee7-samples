package org.javaee7.jms.batch;

import org.javaee7.util.BatchTestHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.JobExecution;
import javax.ejb.EJB;
import javax.jms.*;
import java.util.Properties;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * This test demonstrates programmatical creation of durable consumer, and reading
 * its subscribed messages in a batch job in form of an +ItemReader+.
 *
 * include::JmsItemReader[]
 *
 * The items are then fed into the writer, that performs the aggregation and stores
 * the result into a +@Singleton+ EJB.
 *
 * include::SummingItemWriter[]
 *
 * @author Patrik Dudits
 */
@RunWith(Arquillian.class)
public class JmsItemReaderTest {

    /**
     * Upon deployment a topic and connection factory for durable subscription are created:
     *
     * include::Resources[]
     *
     * Then the subscription itself is created by means of +@Singleton+ +@Startup+ EJB
     * +SubscriptionCreator+.
     *
     * include::SubscriptionCreator#createSubscription[]
     *
     * The job itself computes sum and count of random numbers that are send on the topic.
     * Note that at time of sending there is no active consumer listening on the topic.
     */
    @Deployment
    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
            .addClass(BatchTestHelper.class)
            .addPackage(JmsItemReader.class.getPackage())
            .addAsResource("META-INF/batch-jobs/jms-job.xml");
    }

    @Resource(lookup = "java:comp/DefaultJMSConnectionFactory")
    ConnectionFactory factory;

    @Resource(lookup = Resources.TOPIC)
    Topic topic;

    @EJB
    ResultCollector collector;

    /**
     * In this test case we verify that the subscription is really created upon deployment
     * and thus messages are waiting for the job even before the first run of it.
     *
     * The subscription is not deleted even after the application is undeployed, because
     * the physical topic and its subscription in the message broker still exist,
     * even after the application scoped managed objects are deleted.
     *
     * Following method is used to generate the payload:
     *
     * include::JmsItemReaderTest#sendMessages[]
     *
     * So we send 10 random numbers, and verify that summing integers works exactly the
     * same way on both ends. Or that the job really picked up all the numbers submitted
     * for the computation.
     */
    @InSequence(1)
    @Test
    public void worksAfterDeployment() throws InterruptedException {
        int sum = sendMessages(10);
        runJob();
        assertEquals(10, collector.getLastItemCount());
        assertEquals(sum, collector.getLastSum());
        assertEquals(1, collector.getNumberOfJobs());
    }

    /**
     *  To verify that the durable subscription really collects messages we do few
     *  more runs.
     */
    @InSequence(2)
    @Test
    public void worksInMultipleRuns() throws InterruptedException {
        int sum = sendMessages(14);
        runJob();
        assertEquals(14, collector.getLastItemCount());
        assertEquals(sum, collector.getLastSum());
        assertEquals(2, collector.getNumberOfJobs());
        sum = sendMessages(8); // <1> Sending messages from separate connections makes no difference
        sum += sendMessages(4);
        runJob();
        assertEquals(12, collector.getLastItemCount());
        assertEquals(sum, collector.getLastSum());
        assertEquals(3, collector.getNumberOfJobs());
    }

    private void runJob() throws InterruptedException {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("jms-job", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchTestHelper.keepTestAlive(jobExecution);
    }

    private int sendMessages(int count) {
        int sum = 0;
        Random r = new Random();
        try (JMSContext jms = factory.createContext(Session.AUTO_ACKNOWLEDGE)) {
            JMSProducer producer = jms.createProducer();
            for (int i = 0; i < count; i++) {
                int payload = r.nextInt();
                producer.send(topic, payload);
                sum += payload;
            }
        }
        return sum;
    }
}
