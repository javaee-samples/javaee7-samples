package org.javaee7.concurrency.managedexecutor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.annotation.Resource;
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
    
    Callable<Product> callableTask;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class).
                addClasses(MyRunnableTask.class,
                        MyCallableTask.class,
                        Product.class,
                        TestStatus.class);
    }

    Collection<Callable<Product>> callableTasks = new ArrayList<>();

    @Before
    public void setup() throws NamingException {
        InitialContext ctx = new InitialContext();
        defaultExecutor = (ManagedExecutorService) ctx.lookup("java:comp/DefaultManagedExecutorService");
//        executorFromWebXml = (ManagedExecutorService) ctx.lookup("java:comp/env/concurrent/myExecutor");

        callableTask = new MyCallableTask(1);
        for (int i = 0; i < 5; i++) {
            callableTasks.add(new MyCallableTask(i));
        }
        TestStatus.invokedRunnable = false;
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
