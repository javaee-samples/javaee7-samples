package org.javaee7.concurrency.managedscheduledexecutor;

import java.util.concurrent.Callable;

/**
 * @author Arun Gupta
 */
public class MyCallableTask implements Callable<Product> {
    
    private int id;

    public MyCallableTask() {
    }

    public MyCallableTask(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public Product call() {
        System.out.println("Running Callable Task: " + id);
        return new Product(id);
    }
}
