package org.javaee7.concurrency.managedscheduledexecutor;

/**
 * @author Arun Gupta
 */
public class MyRunnableTask implements Runnable {

    private int id;

    public MyRunnableTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Running Runnable Task: " + id);
    }
}
