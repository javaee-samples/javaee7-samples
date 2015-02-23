package org.javaee7.batch.sample.chunk.mapper;

import java.io.Serializable;
import javax.batch.api.partition.PartitionCollector;

/**
 * @author Arun Gupta
 */
public class MyCollector implements PartitionCollector {

    @Override
    public Serializable collectPartitionData() throws Exception {
        System.out.println("collectPartitionData");

        return new Serializable() {

        };
    }

}
