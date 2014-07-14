package org.javaee7.concurrency.managedexecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

/**
 * @author Arun Gupta
 */
@Stateless
public class TestBean {
    @Resource(name = "DefaultManagedExecutorService")
    ManagedExecutorService executor;

    public boolean doSomething() throws InterruptedException {
        TestStatus.latch = new CountDownLatch(1);
        executor.submit(new Runnable() {

            @Override
            public void run() {
                TestStatus.latch.countDown();
            }
        });
        TestStatus.latch.await(2000, TimeUnit.MILLISECONDS);
        return true;
    }
}
