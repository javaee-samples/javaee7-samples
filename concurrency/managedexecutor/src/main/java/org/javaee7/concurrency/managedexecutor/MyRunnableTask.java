package org.javaee7.concurrency.managedexecutor;

/**
 * @author Arun Gupta
 */
public class MyRunnableTask implements Runnable {

    @Override
    public void run() {
        TestStatus.latch.countDown();
    }
}
