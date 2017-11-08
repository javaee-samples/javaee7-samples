package org.javaee7.cdi.dynamic.interceptor.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

/**
 * 
 * @author Arjan Tijms
 * 
 * This class installs the dynamic interceptor
 *
 */
public class CdiExtension implements Extension {
    
    /**
     * This method registers the (annotated) class that enables the interceptor and sets its priority
     * 
     */
    public void register(@Observes BeforeBeanDiscovery beforeBean, BeanManager beanManager) {
        beforeBean.addAnnotatedType(
                beanManager.createAnnotatedType(HelloInterceptorEnabler.class), 
                "CdiExtension" + HelloInterceptorEnabler.class);
    }

    /**
     * This method registers the actual dynamic interceptor
     */
    public void afterBean(final @Observes AfterBeanDiscovery afterBeanDiscovery) {
        afterBeanDiscovery.addBean(new DynamicHelloInterceptor());
    }
    
}
