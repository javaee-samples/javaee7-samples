package org.javaee7.websocket.injection;

import java.util.logging.Logger;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Arun Gupta
 */
@Interceptor
@Logging
public class LoggingInterceptor {

    @AroundInvoke
    public Object log(InvocationContext context)
        throws Exception {
        Logger.getLogger(getClass().getName()).info(context.getMethod().getName());
        Logger.getLogger(getClass().getName()).info(context.getParameters().toString());
        return context.proceed();
    }
}
