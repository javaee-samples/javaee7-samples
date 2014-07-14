package org.javaee7.concurrency.managedexecutor;

import java.util.Map;
import java.util.concurrent.Future;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedTaskListener;

/**
 * @author Arun Gupta
 */
public class MyTaskWithListener implements Runnable, ManagedTask, ManagedTaskListener {

    private int id;

    public MyTaskWithListener() {
    }

    public MyTaskWithListener(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        TestStatus.latch.countDown();
    }

    @Override
    public void taskAborted(Future<?> future, ManagedExecutorService mes, Object o, Throwable t) {
        System.out.println("aborted");
    }

    @Override
    public void taskDone(Future<?> future, ManagedExecutorService mes, Object o, Throwable t) {
        System.out.println("done");
    }

    @Override
    public void taskStarting(Future<?> future, ManagedExecutorService mes, Object o) {
        System.out.println("starting");
    }

    @Override
    public void taskSubmitted(Future<?> future, ManagedExecutorService mes, Object o) {
        System.out.println("submitted");
    }

    @Override
    public ManagedTaskListener getManagedTaskListener() {
        return this;
    }

    @Override
    public Map<String, String> getExecutionProperties() {
        return null;
    }
}
