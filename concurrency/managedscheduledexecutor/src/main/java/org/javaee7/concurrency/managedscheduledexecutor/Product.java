package org.javaee7.concurrency.managedscheduledexecutor;

/**
 * @author Arun Gupta
 */
public class Product {
    private int id;

    public Product() {
    }

    public Product(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
