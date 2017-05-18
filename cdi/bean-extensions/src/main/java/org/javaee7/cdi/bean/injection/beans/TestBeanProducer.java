package org.javaee7.cdi.bean.injection.beans;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import static java.util.Arrays.asList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;

/**
 * @author Matt Gill
 */
public class TestBeanProducer implements Bean<TestBean> {

    private final InjectionTarget<TestBean> injectionTarget;

    public TestBeanProducer(InjectionTarget<?> target) {
        this.injectionTarget = (InjectionTarget<TestBean>) target;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Type> getTypes() {
        Set<Type> types = new HashSet<>();
        types.add(TestBean.class);
        types.add(Object.class);
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        Set<Annotation> qualifiers = new HashSet<>();
        qualifiers.add(new AnnotationLiteral<Default>() {
        });
        qualifiers.add(new AnnotationLiteral<Any>() {
        });
        return qualifiers;
    }

    @Override
    public Class<?> getBeanClass() {
        return TestBean.class;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return Dependent.class;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return injectionTarget.getInjectionPoints();
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public TestBean create(CreationalContext<TestBean> creationalContext) {
        TestBean bean = injectionTarget.produce(creationalContext);
        injectionTarget.inject(bean, creationalContext);
        injectionTarget.postConstruct(bean);
        return bean;
    }

    @Override
    public void destroy(TestBean instance, CreationalContext<TestBean> creationalContext) {
        injectionTarget.preDestroy(instance);
        injectionTarget.dispose(instance);
        creationalContext.release();
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @SafeVarargs
    protected static <T> Set<T> asSet(T... a) {
        return new HashSet<>(asList(a));
    }

}
