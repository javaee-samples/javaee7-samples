package org.javaee7.cdi.bean.injection.extension;

import org.javaee7.cdi.bean.injection.beans.TestBeanProducer;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import org.javaee7.cdi.bean.injection.beans.impl.AfterBeanDiscoveryBean;

/**
 * @author Matt Gill
 */
public class BeanExtension implements Extension {

    void beforeBeanDiscovery(BeanManager bm, @Observes BeforeBeanDiscovery event) {
        final AnnotatedType<?> type = bm.createAnnotatedType(AfterBeanDiscoveryBean.class);

        event.addAnnotatedType(type);
    }

    void afterTypeDiscovery(BeanManager bm, @Observes AfterTypeDiscovery event) {
        final AnnotatedType<?> type = bm.createAnnotatedType(AfterBeanDiscoveryBean.class);

        event.addAnnotatedType(type, type.getJavaClass().getName() + "#");
    }

    void afterBeanDiscovery(BeanManager bm, @Observes AfterBeanDiscovery event) {
        final AnnotatedType<AfterBeanDiscoveryBean> type = bm.createAnnotatedType(AfterBeanDiscoveryBean.class);
        
        final InjectionTarget<AfterBeanDiscoveryBean> it = bm.createInjectionTarget(type);
        
        event.addBean(new TestBeanProducer(it));
    }
}
