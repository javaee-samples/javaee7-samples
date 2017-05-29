package org.javaee7.jms.xa.producers;

import javax.annotation.Resource;
import javax.enterprise.inject.Produces;
import javax.jms.ConnectionFactory;

public class NonXAConnectionFactoryProducer {

    @Produces
    @Resource(lookup = "java:app/jms/nonXAconnectionFactory")
    private ConnectionFactory connectionFactory;
}
