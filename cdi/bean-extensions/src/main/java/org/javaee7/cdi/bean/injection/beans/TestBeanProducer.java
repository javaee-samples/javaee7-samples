package org.javaee7.cdi.bean.injection.beans;

import org.javaee7.cdi.bean.injection.beans.impl.AfterBeanDiscoveryBean;
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
public class TestBeanProducer implements Bean<AfterBeanDiscoveryBean> {
    
    private InjectionTarget<AfterBeanDiscoveryBean> injectionTarget;

    public TestBeanProducer(InjectionTarget<AfterBeanDiscoveryBean> target) {
        this.injectionTarget = target;
    }

    @Override
    public String getName() {
        return "testBean";
    }

    @Override
    public Set<Type> getTypes() {
        Set<Type> types = new HashSet<>();
        types.add(AfterBeanDiscoveryBean.class);
        types.add(Object.class);
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        Set<Annotation> qualifiers = new HashSet<>();
        qualifiers.add(new AnnotationLiteral<Default>() {});
        qualifiers.add(new AnnotationLiteral<Any>() {});
        return qualifiers;
    }

    @Override
    public Class<?> getBeanClass() {
        return AfterBeanDiscoveryBean.class;
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
    public AfterBeanDiscoveryBean create(CreationalContext<AfterBeanDiscoveryBean> creationalContext) {
        AfterBeanDiscoveryBean bean = injectionTarget.produce(creationalContext);
        injectionTarget.inject(bean, creationalContext);
        injectionTarget.postConstruct(bean);
        return bean;
    }

    @Override
    public void destroy(AfterBeanDiscoveryBean instance, CreationalContext<AfterBeanDiscoveryBean> creationalContext) {
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
