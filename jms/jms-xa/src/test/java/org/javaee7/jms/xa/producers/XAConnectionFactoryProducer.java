package org.javaee7.jms.xa.producers;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.jms.ConnectionFactory;

public class XAConnectionFactoryProducer {

    @Produces
    @Resource(lookup = "java:/JmsXA")
    private ConnectionFactory connectionFactory;
}
