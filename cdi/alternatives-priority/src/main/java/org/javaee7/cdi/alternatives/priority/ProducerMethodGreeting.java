package org.javaee7.cdi.alternatives.priority;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Produces;

/**
 * @author Radim Hanus
 */
@Priority(3000)
public class ProducerMethodGreeting {

    @Produces
    @Alternative
    public Greeting getGreeting() {
        return new SimpleGreeting();
    }
}
