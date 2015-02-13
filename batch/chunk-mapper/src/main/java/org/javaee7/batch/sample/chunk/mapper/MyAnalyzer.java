package org.javaee7.batch.sample.chunk.mapper;

import java.io.Serializable;
import javax.batch.api.partition.PartitionAnalyzer;
import javax.batch.runtime.BatchStatus;

/**
 * @author Arun Gupta
 */
public class MyAnalyzer implements PartitionAnalyzer {

    @Override
    public void analyzeCollectorData(Serializable srlzbl) throws Exception {
        System.out.println("analyzeCollectorData");
    }

    @Override
    public void analyzeStatus(BatchStatus bs, String string) throws Exception {
        System.out.println("analyzeStatus");
    }

}
