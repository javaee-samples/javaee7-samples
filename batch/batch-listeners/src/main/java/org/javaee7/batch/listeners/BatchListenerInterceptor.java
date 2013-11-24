package org.javaee7.batch.listeners;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Roberto Cortez
 */
@BatchListener
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class BatchListenerInterceptor {
    @Inject
    private BatchListenerRecorder batchListenerRecorder;

    @AroundInvoke
    private Object recordListenersExecution(InvocationContext ctx) throws Exception {
        batchListenerRecorder.addListenerMethodExecution(ctx.getMethod().getDeclaringClass(),
                                                         ctx.getMethod().getName());
        return ctx.proceed();
    }
}
