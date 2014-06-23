package org.javaee7.batch.sample.chunk.mapper;

import javax.batch.api.partition.PartitionReducer;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyReducer implements PartitionReducer {

    @Override
    public void beginPartitionedStep() throws Exception {
        System.out.println("beginPartitionedStep");
    }

    @Override
    public void beforePartitionedStepCompletion() throws Exception {
        System.out.println("beforePartitionedStepCompletion");
    }

    @Override
    public void rollbackPartitionedStep() throws Exception {
        System.out.println("rollbackPartitionedStep");
    }

    @Override
    public void afterPartitionedStepCompletion(PartitionStatus ps) throws Exception {
        System.out.println("afterPartitionedStepCompletion");
    }
    
}
