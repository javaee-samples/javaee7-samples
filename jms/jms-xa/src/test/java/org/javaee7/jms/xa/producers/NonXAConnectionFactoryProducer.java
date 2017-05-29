package org.javaee7.jms.xa.producers;

import javax.annotation.Resource;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.jms.ConnectionFactory;

public class ConnectionFactoryProducer {

    @Default
    @Produces
    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
}
