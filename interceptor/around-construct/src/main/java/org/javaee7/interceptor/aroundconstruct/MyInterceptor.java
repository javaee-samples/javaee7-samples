package org.javaee7.interceptor.aroundconstruct;

import javax.interceptor.AroundConstruct;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author Radim Hanus
 */
@Interceptor
@MyInterceptorBinding
public class MyInterceptor {
    @AroundConstruct
    public Object onConstruct(InvocationContext context) throws Exception {
        // null before the InvocationContext.proceed() returns
        Object target = context.getTarget();
        isNull(target);
        // null in case of AroundConstruct
        Method method = context.getMethod();
        isNull(method);
        // NOT null in case of AroundConstruct
        Constructor ctor = context.getConstructor();
        isNotNull(ctor);

        // perform the constructor injection
        Object result = context.proceed();
        isNull(result);

        // NOT null after the InvocationContext.proceed() completes
        target = context.getTarget();
        isNotNull(target);
        // a constructor should have been called
        GreetingBean bean = (GreetingBean) target;
        isBoolean(bean.isConstructed(), true);
        isBoolean(bean.isInitialized(), false);
        // constructor injection should have been done
        isNotNull(bean.getParam());

        return null;
    }

    private static void isNull(Object o) throws Exception {
        if (o != null) {
            throw new IllegalStateException("null required");
        }
    }

    private static void isNotNull(Object o) throws Exception {
        if (o == null) {
            throw new IllegalStateException("not null required");
        }
    }

    private static void isBoolean(Object o, Boolean value) {
        if (!o.equals(value)) {
            throw new IllegalStateException(value + " required");
        }
    }
}
