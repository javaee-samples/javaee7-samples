package org.javaee7.cdi.interceptors.priority;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Interceptors with smaller priority values are called first.
 *
 * @author Radim Hanus
 */
@Interceptor
@MyInterceptorBinding
@Priority(Interceptor.Priority.APPLICATION + 100)
public class HighPriorityInterceptor {
    @AroundInvoke
    public Object log(InvocationContext context) throws Exception {
        Object[] parameters = context.getParameters();
        if (parameters.length > 0 && parameters[0] instanceof String) {
            String param = (String) parameters[0];
            parameters[0] = "Hi " + param + " !";
            context.setParameters(parameters);
        }
        return context.proceed();
    }
}
