package org.javaee7.jms.batch;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * @author Patrik Dudits
 */
@Named
public class SummingItemWriter extends AbstractItemWriter {
    @Inject
    ResultCollector collector;

    private int numItems;
    private int sum;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        numItems = 0; // <1> Reset the computation
        sum = 0;
    }

    @Override
    public void writeItems(List<Object> objects) throws Exception {
        numItems += objects.size(); // <2> Perform the computation. Note that this may be called multiple times within single job run
        sum += computeSum(objects);
    }

    @Override
    public void close() throws Exception {
        collector.postResult(sum, numItems); // <3> Post results
    }

    private int computeSum(List<Object> objects) {
        int subTotal = 0;
        for (Object o : objects) {
            subTotal += (Integer) o;
        }
        return subTotal;
    }
}
