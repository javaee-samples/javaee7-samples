package org.javaee7.concurrency.dynamicproxy;

/**
 * @author Arun Gupta
 */
public class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("MyRunnable.run");
    }
}
