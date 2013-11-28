package org.javaee7.concurrency.managedexecutor;

import java.util.concurrent.CountDownLatch;

/**
 * @author Arun Gupta
 */
public class TestStatus {
    public static CountDownLatch latch;
    public static boolean foundTransactionScopedBean;
}
