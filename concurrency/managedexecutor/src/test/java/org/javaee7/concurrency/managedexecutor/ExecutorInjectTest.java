package org.javaee7.concurrency.managedexecutor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
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
    @Inject
    // Inject so we have a managed bean to handle the TX
    MyTaskWithTransaction taskWithTransaction;
    Collection<Callable<Product>> callableTasks = new ArrayList<>();

    private static CountDownLatch latch;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
            addClasses(MyRunnableTask.class,
                MyCallableTask.class,
                Product.class,
                TestStatus.class,
                MyTaskWithListener.class,
                MyTaskWithTransaction.class,
                MyTransactionScopedBean.class,
                TestBean.class).
            setWebXML(new FileAsset(new File("src/main/webapp/WEB-INF/web.xml"))).
            addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); // Adding beans.xml shouldn't be required? WildFly Beta1
    }

    @Before
    public void setup() {
        callableTask = new MyCallableTask(1);
        runnableTask = new MyRunnableTask();
        taskWithListener = new MyTaskWithListener(1);
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

    @Test
    public void testSubmitWithRunnableNoName() throws Exception {
        TestStatus.latch = new CountDownLatch(1);
        executorNoName.submit(runnableTask);
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testSubmitWithCallableNoName() throws Exception {
        TestStatus.latch = new CountDownLatch(1);
        Future<Product> future = executorNoName.submit(callableTask);
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
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
        TestStatus.latch = new CountDownLatch(1);
        executorFromWebXml.submit(new MyRunnableTask());
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testSubmitWithCallableFromWebXML() throws Exception {
        TestStatus.latch = new CountDownLatch(1);
        Future<Product> future = executorFromWebXml.submit(new MyCallableTask(1));
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
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
        TestStatus.latch = new CountDownLatch(1);
        defaultExecutor.submit(taskWithListener);
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
    }

    @Test
    public void testSubmitWithTransaction() throws Exception {
        TestStatus.latch = new CountDownLatch(1);
        defaultExecutor.submit(taskWithTransaction);
        assertTrue(TestStatus.latch.await(2000, TimeUnit.MILLISECONDS));
        assertTrue(TestStatus.foundTransactionScopedBean);
    }

    @Test
    public void testSubmitWithEJB() throws Exception {
        assertTrue(ejb.doSomething());
    }

}
