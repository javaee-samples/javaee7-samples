package org.javaee7.jms.temp.destination;

import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;

/**
 * Application scoped JMS resources for the samples.
 * @author Patrik Dudits
 */
@JMSDestinationDefinitions({
    @JMSDestinationDefinition(
        name = Resources.REQUEST_QUEUE,
        resourceAdapter = "jmsra",
        interfaceName = "javax.jms.Queue",
        destinationName = "requestQueue",
        description = "Queue for service requests"),
})
public class Resources {
    public static final String REQUEST_QUEUE = "java:global/jms/requestQueue";
}
