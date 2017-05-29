package org.javaee7.jms.xa;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Allows test to wait until a method is invoked. Note that this gets applied as EJB interceptor, and therefore
 * returning from {@link #waitFor(Class, String) } does not guarantee that the bean's transaction
 * is already committed.
 *
 * @author Patrik Dudits
 */
public class ReceptionSynchronizer {

    private final static Map<Method, CountDownLatch> barrier = new HashMap<>();

    public static void clear()
    {
        barrier.clear();
    }

    public static void waitFor(Class<?> clazz, String methodName) throws InterruptedException
    {
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
        waitFor(method);
    }

    private static void waitFor(Method method) throws InterruptedException
    {
        CountDownLatch latch = null;
        synchronized (barrier) {
            if (barrier.containsKey(method)) {
                latch = barrier.get(method);
                if (latch.getCount() == 0) {
                    throw new IllegalStateException("The invocation already happened");
                } else {
                    throw new IllegalStateException("Sorry, I only serve the first one");
                }
            } else {
                latch = new CountDownLatch(1);
                barrier.put(method, latch);
            }
        }
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new AssertionError("Expected method has not been invoked");
        }
    }

    @AroundInvoke
    public Object invoke(InvocationContext ctx) throws Exception
    {
        try {
            System.out.println("Intercepting " + ctx.getMethod().toGenericString());
            return ctx.proceed();
        } finally {
            registerInvocation(ctx.getMethod());
        }
    }

    void registerInvocation(Method m)
    {
        CountDownLatch latch = null;
        synchronized (barrier) {
            if (barrier.containsKey(m)) {
                latch = barrier.get(m);
            }
        }
        if (latch != null) {
            latch.countDown();
        }
    }
}
