package org.javaee7.cdi.dynamic.interceptor.extension;

import static java.util.Collections.singleton;
import static javax.enterprise.inject.spi.InterceptionType.AROUND_INVOKE;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.InterceptionType;
import javax.enterprise.util.AnnotationLiteral;
import javax.interceptor.InvocationContext;

public class DynamicHelloInterceptor extends DynamicInterceptorBase<HelloInterceptorEnabler> {

    @SuppressWarnings("all")
    public static class HelloAnnotationLiteral extends AnnotationLiteral<Hello> implements Hello {
        private static final long serialVersionUID = 1L;
    }

    /**
     * The Intercept binding this dynamic interceptor is doing its work for
     */
    public Set<Annotation> getInterceptorBindings() {
        return singleton((Annotation) new HelloAnnotationLiteral());
    }

    /**
     * The type of intercepting being done, corresponds to <code>@AroundInvoke</code> etc on 
     * "static" interceptors
     */
    public boolean intercepts(InterceptionType type) {
        return AROUND_INVOKE.equals(type);
    }
    
    /**
     * The annotated class that contains the priority and causes the interceptor to be enabled
     */
    public Class<?> getBeanClass() {
        return HelloInterceptorEnabler.class;
    }

    public Object intercept(InterceptionType type, HelloInterceptorEnabler enabler, InvocationContext ctx) {
        try {
            return "Hello, " + ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    

}
