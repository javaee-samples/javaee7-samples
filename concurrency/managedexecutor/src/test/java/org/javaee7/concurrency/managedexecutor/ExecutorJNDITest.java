package org.javaee7.concurrency.managedexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class ExecutorJNDITest {

    ManagedExecutorService defaultExecutor;

    ManagedExecutorService executorFromWebXml;

    Runnable runnableTask;
    Callable<Product> callableTask;
    Collection<Callable<Product>> callableTasks = new ArrayList<>();

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addClasses(MyRunnableTask.class,
                MyCallableTask.class,
                Product.class,
                TestStatus.class);
    }

    @Before
    public void setup() throws NamingException {
        InitialContext ctx = new InitialContext();
        defaultExecutor = (ManagedExecutorService) ctx.lookup("java:comp/DefaultManagedExecutorService");
        //        executorFromWebXml = (ManagedExecutorService) ctx.lookup("java:comp/env/concurrent/myExecutor");

        runnableTask = new MyRunnableTask();
        callableTask = new MyCallableTask(1);
        for (int i = 0; i < 5; i++) {
            callableTasks.add(new MyCallableTask(i));
        }
    }

    @Test
    public void testSubmitWithRunnableDefault() throws Exception {
        TestStatus.latch = new CountDownLatch(1);
        defaultExecutor.submit(runnableTask);
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testSubmitWithCallableDefault() throws Exception {
        TestStatus.latch = new CountDownLatch(1);
        Future<Product> future = defaultExecutor.submit(callableTask);
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
        assertEquals(1, future.get().getId());
    }

    @Test
    public void testInvokeAllWithCallableDefault() throws Exception {
        List<Future<Product>> results = defaultExecutor.invokeAll(callableTasks);
        int count = 0;
        for (Future<Product> f : results) {
            assertEquals(count++, f.get().getId());
        }
    }

    @Test
    public void testInvokeAnyWithCallableDefault() throws Exception {
        Product results = defaultExecutor.invokeAny(callableTasks);
        assertTrue(results.getId() >= 0);
        assertTrue(results.getId() <= 5);
    }

    //    @Test
    //    public void testSubmitWithRunnableFromWebXML() throws Exception {
    //        executorFromWebXml.submit(new MyRunnableTask(1));
    //        Thread.sleep(2000);
    //        assertTrue(TestStatus.invokedRunnable);
    //    }
    //
    //    @Test
    //    public void testSubmitWithCallableFromWebXML() throws Exception {
    //        Future<Product> future = executorFromWebXml.submit(callableTask);
    //        assertEquals(1, future.get().getId());
    //    }
    //
    //    @Test
    //    public void testInvokeAllWithCallableFromWebXML() throws Exception {
    //        List<Future<Product>> results = executorFromWebXml.invokeAll(callableTasks);
    //        int count = 0;
    //        for (Future<Product> f : results) {
    //            assertEquals(count++, f.get().getId());
    //        }
    //    }
    //
    //    @Test
    //    public void testInvokeAnyWithCallableFromWebXML() throws Exception {
    //        Product results = executorFromWebXml.invokeAny(callableTasks);
    //        assertTrue(results.getId() >= 0);
    //        assertTrue(results.getId() <= 5);
    //    }
}
