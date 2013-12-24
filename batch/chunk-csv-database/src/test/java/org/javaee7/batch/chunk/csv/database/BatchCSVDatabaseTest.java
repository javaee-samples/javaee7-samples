package org.javaee7.batch.chunk.csv.database;

import org.javaee7.util.BatchTestHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @author Roberto Cortez
 */
@RunWith(Arquillian.class)
public class BatchCSVDatabaseTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClass(BatchTestHelper.class)
                .addPackage("org.javaee7.batch.chunk.csv.database")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addAsResource("META-INF/batch-jobs/myJob.xml")
                .addAsResource("META-INF/persistence.xml")
                .addAsResource("META-INF/create.sql")
                .addAsResource("META-INF/drop.sql")
                .addAsResource("META-INF/mydata.csv");
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    public void testBatchCSVDatabase() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("myJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        BatchTestHelper.keepTestAlive(jobExecution);

        List<StepExecution> stepExecutions = jobOperator.getStepExecutions(executionId);
        for (StepExecution stepExecution : stepExecutions) {
            if (stepExecution.getStepName().equals("myStep")) {
                Map<Metric.MetricType, Long> metricsMap = BatchTestHelper.getMetricsMap(stepExecution.getMetrics());
                System.out.println(metricsMap);

                assertEquals(7L, (long) metricsMap.get(Metric.MetricType.READ_COUNT));
                assertEquals(7L, (long) metricsMap.get(Metric.MetricType.WRITE_COUNT));
                assertEquals(3L, (long) metricsMap.get(Metric.MetricType.COMMIT_COUNT));
            }
        }

        Query query = entityManager.createQuery("SELECT p FROM Person p");
        @SuppressWarnings("unchecked")
        List<Person> persons = query.getResultList();

        assertEquals(7L, persons.size());
        assertEquals(jobExecution.getBatchStatus(), BatchStatus.COMPLETED);
    }
}