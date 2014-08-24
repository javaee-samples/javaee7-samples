package org.javaee7.concurrency.managedthreadfactory;

/**
 * @author Arun Gupta
 */
public class MyTask implements Runnable {

    private int id;

    public MyTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        System.out.println("Running Task: " + id);
    }
}
