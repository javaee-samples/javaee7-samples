package org.javaee7.jms.batch;

import javax.annotation.Resource;
import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Topic;
import java.io.Serializable;

/**
 * @author Patrik Dudits
 */
@Named
public class JmsItemReader extends AbstractItemReader {

    @Resource(lookup = Resources.CONNECTION_FACTORY)
    ConnectionFactory factory;

    private JMSContext jms;

    @Resource(lookup = Resources.TOPIC)
    Topic topic;

    private JMSConsumer subscription;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        jms = factory.createContext(); // <1> Since we're not using default connection factory, we use app managed +JMSContext+
        subscription = jms.createDurableConsumer(topic, Resources.SUBSCRIPTION);
    }

    @Override
    public Object readItem() throws Exception {
        Integer item = subscription.receiveBodyNoWait(Integer.class); // <2> When there is no message ready to be received, +null+ is returned, fulfilling +readItem+ contract
        return item;
    }

    @Override
    public void close() throws Exception {
        subscription.close(); // <3> Free resources at end of run
        jms.close();
    }

}
