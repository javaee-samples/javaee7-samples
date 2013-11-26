package org.javaee7.concurrency.managedexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
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
public class ExecutorInjectTest {

    @Resource(name = "DefaultManagedExecutorService")
    ManagedExecutorService defaultExecutor;

    @Resource(name = "concurrent/myExecutor")
    ManagedExecutorService executorFromWebXml;

    @Resource
    ManagedExecutorService executorNoName;

    @EJB
    TestBean ejb;

    Callable<Product> callableTask;
    Runnable runnableTask;
    MyTaskWithListener taskWithListener;
    MyTaskWithTransaction taskWithTransaction;
    Collection<Callable<Product>> callableTasks = new ArrayList<>();

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
                addClasses(MyRunnableTask.class,
                        MyCallableTask.class,
                        Product.class,
                        TestStatus.class,
                        MyTaskWithListener.class,
                        MyTaskWithTransaction.class,
                        TestBean.class);
    }

    @Before
    public void setup() {
        callableTask = new MyCallableTask(1);
        runnableTask = new MyRunnableTask(1);
        taskWithListener = new MyTaskWithListener(1);
        for (int i = 0; i < 5; i++) {
            callableTasks.add(new MyCallableTask(i));
        }
        TestStatus.invokedRunnable = false;
        TestStatus.invokedTaskWithListener = false;
        TestStatus.invokedTaskWithTransaction = false;
    }

    @Test
    public void testSubmitWithRunnableDefault() throws Exception {
        defaultExecutor.submit(new MyRunnableTask(1));
        Thread.sleep(2000);
        assertTrue(TestStatus.invokedRunnable);
    }

    @Test
    public void testSubmitWithCallableDefault() throws Exception {
        Future<Product> future = defaultExecutor.submit(callableTask);
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

    @Test
    public void testSubmitWithRunnableNoName() throws Exception {
        executorNoName.submit(new MyRunnableTask(1));
        Thread.sleep(2000);
        assertTrue(TestStatus.invokedRunnable);
    }

    @Test
    public void testSubmitWithCallableNoName() throws Exception {
        Future<Product> future = executorNoName.submit(callableTask);
        assertEquals(1, future.get().getId());
    }

    @Test
    public void testInvokeAllWithCallableNoName() throws Exception {
        List<Future<Product>> results = executorNoName.invokeAll(callableTasks);
        int count = 0;
        for (Future<Product> f : results) {
            assertEquals(count++, f.get().getId());
        }
    }

    @Test
    public void testInvokeAnyWithCallableNoName() throws Exception {
        Product results = executorNoName.invokeAny(callableTasks);
        assertTrue(results.getId() >= 0);
        assertTrue(results.getId() <= 5);
    }

    @Test
    public void testSubmitWithRunnableFromWebXML() throws Exception {
        executorFromWebXml.submit(runnableTask);
        Thread.sleep(2000);
        assertTrue(TestStatus.invokedRunnable);
    }

    @Test
    public void testSubmitWithCallableFromWebXML() throws Exception {
        Future<Product> future = executorFromWebXml.submit(callableTask);
        assertEquals(1, future.get().getId());
    }

    @Test
    public void testInvokeAllWithCallableFromWebXML() throws Exception {
        List<Future<Product>> results = executorFromWebXml.invokeAll(callableTasks);
        int count = 0;
        for (Future<Product> f : results) {
            assertEquals(count++, f.get().getId());
        }
    }

    @Test
    public void testInvokeAnyWithCallableFromWebXML() throws Exception {
        Product results = executorFromWebXml.invokeAny(callableTasks);
        assertTrue(results.getId() >= 0);
        assertTrue(results.getId() <= 5);
    }

    @Test
    public void testSubmitWithListener() throws Exception {
        defaultExecutor.submit(taskWithListener);
        Thread.sleep(2000);
        assertTrue(TestStatus.invokedTaskWithListener);
    }

    @Test
    public void testSubmitWithTransaction() throws Exception {
        defaultExecutor.submit(taskWithTransaction);
        Thread.sleep(2000);
        assertTrue(TestStatus.invokedTaskWithTransaction);
    }

    @Test
    public void testSubmitWithEJB() throws Exception {
        ejb.run();
        Thread.sleep(2000);
        assertTrue(TestStatus.invokedRunnable);
    }

}
