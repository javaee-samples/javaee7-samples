package org.javaee7.jms.send.receive.mdb;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.transaction.Status;
import javax.transaction.Synchronization;
import javax.transaction.TransactionSynchronizationRegistry;

/**
 * Allows test to wait until a method is invoked.
 *
 * @author Patrik Dudits
 */
public class ReceptionSynchronizer {

    private final static Map<Method, CountDownLatch> barrier = new HashMap<>();

    @Resource
    TransactionSynchronizationRegistry txRegistry;

    @AroundInvoke
    public Object invoke(final InvocationContext ctx) throws Exception {
        boolean transactional = false;
        try {
            System.out.println("Intercepting " + ctx.getMethod().toGenericString());
            transactional = txRegistry != null && txRegistry.getTransactionStatus() != Status.STATUS_NO_TRANSACTION;
            if (transactional) {
                txRegistry.registerInterposedSynchronization(new Synchronization() {
                    @Override
                    public void beforeCompletion() {

                    }

                    @Override
                    public void afterCompletion(int i) {
                        registerInvocation(ctx.getMethod());
                    }
                });
            }
            return ctx.proceed();
        } finally {
            if (!transactional) {
                registerInvocation(ctx.getMethod());
            }
        }
    }

    void registerInvocation(Method m) {
        CountDownLatch latch = null;
        synchronized (barrier) {
            if (barrier.containsKey(m)) {
                latch = barrier.get(m);
            } else {
                barrier.put(m, new CountDownLatch(0));
            }
        }
        if (latch != null) {
            latch.countDown();
        }
    }

    public static void waitFor(Class<?> clazz, String methodName, int timeoutInMillis) throws InterruptedException {
        Method method = null;
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (methodName.equals(declaredMethod.getName())) {
                if (method == null) {
                    method = declaredMethod;
                } else {
                    throw new IllegalArgumentException(methodName + " is not unique in " + clazz.getSimpleName());
                }
            }
        }
        if (method == null) {
            throw new IllegalArgumentException(methodName + " not found in " + clazz.getSimpleName());
        }
        waitFor(method, timeoutInMillis);
    }

    private static void waitFor(Method method, int timeoutInMillis) throws InterruptedException {
        CountDownLatch latch;
        synchronized (barrier) {
            if (barrier.containsKey(method)) {
                latch = barrier.get(method);
            } else {
                latch = new CountDownLatch(1);
                barrier.put(method, latch);
            }
        }
        if (!latch.await(timeoutInMillis, TimeUnit.MILLISECONDS)) {
            throw new AssertionError("Expected method has not been invoked in " + timeoutInMillis + "ms");
        }
    }
}
