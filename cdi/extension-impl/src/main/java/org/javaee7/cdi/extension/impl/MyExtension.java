package org.javaee7.cdi.extension.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * @author Arun Gupta
 */
public class MyExtension implements Extension {
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
        Logger.getAnonymousLogger().log(Level.INFO,
                "CDI Extension Processing Annotation -> {0}",
                pat.
                getAnnotatedType().
                getJavaClass().
                getName());
    }
}
