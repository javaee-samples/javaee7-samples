package org.javaee7.concurrency.dynamicproxy;

/**
 * @author Arun Gupta
 */
public class MyRunnableWork implements Runnable , MyWork {

    @Override
    public void run() {
        System.out.println("MyRunnableWork.run");
    }

    @Override
    public void myWork() {
        System.out.println("MyRunnablework.myWork");
    }
}
