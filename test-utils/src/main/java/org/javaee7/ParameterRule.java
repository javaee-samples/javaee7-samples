package org.javaee7;

import org.jboss.arquillian.container.test.api.Deployment;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Helper class for Parametrized tests as described here:
 * http://blog.schauderhaft.de/2012/12/16/writing-parameterized-tests-with-junit-rules/
 *
 * @param <T>
 */
public class ParameterRule<T> implements MethodRule {
    private final List<T> params;

    public ParameterRule(List<T> params) {
        if (params == null || params.size() == 0) {
            throw new IllegalArgumentException("'params' must be specified and have more then zero length!");
        }
        this.params = params;
    }

    @Override
    public Statement apply(final Statement base, final FrameworkMethod method, final Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                boolean runInContainer = getDeploymentMethod(target).getAnnotation(Deployment.class).testable();
                if (runInContainer) {
                    evaluateParametersInContainer(base, target);
                } else {
                    evaluateParametersInClient(base, target);
                }
            }
        };
    }

    private Method getDeploymentMethod(Object target) throws NoSuchMethodException {
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getAnnotation(Deployment.class) != null) return method;
        }
        throw new IllegalStateException("No method with @Deployment annotation found!");
    }

    private void evaluateParametersInContainer(Statement base, Object target) throws Throwable {
        if (isRunningInContainer()) {
            evaluateParamsToTarget(base, target);
        } else {
            ignoreStatementExecution(base);
        }
    }

    private void evaluateParametersInClient(Statement base, Object target) throws Throwable {
        if (isRunningInContainer()) {
            ignoreStatementExecution(base);
        } else {
            evaluateParamsToTarget(base, target);
        }
    }

    private boolean isRunningInContainer() {
        try {
            new InitialContext().lookup("java:comp/env");
            return true;
        } catch (NamingException e) {
            return false;
        }
    }

    private void evaluateParamsToTarget(Statement base, Object target) throws Throwable {
        for (Object param : params) {
            Field targetField = getTargetField(target);
            if (!targetField.isAccessible()) {
                targetField.setAccessible(true);
            }
            targetField.set(target, param);
            base.evaluate();
        }
    }

    private Field getTargetField(Object target) throws NoSuchFieldException {
        Field[] allFields = target.getClass().getDeclaredFields();
        for (Field field : allFields) {
            if (field.getAnnotation(Parameter.class) != null) return field;
        }
        throw new IllegalStateException("No field with @Parameter annotation found! Forgot to add it?");
    }

    private void ignoreStatementExecution(Statement base) {
        try {
            base.evaluate();
        } catch (Throwable ignored) {}
    }
}