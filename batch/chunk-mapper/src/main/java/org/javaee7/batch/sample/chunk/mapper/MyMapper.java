package org.javaee7.batch.sample.chunk.mapper;

import java.util.Properties;
import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
public class MyMapper implements PartitionMapper {

    @Override
    public PartitionPlan mapPartitions() throws Exception {
        return new PartitionPlanImpl() {

            @Override
            public int getPartitions() {
                return 2;
            }

            @Override
            public int getThreads() {
                return 2;
            }

            @Override
            public Properties[] getPartitionProperties() {
                Properties[] props = new Properties[getPartitions()];
                
                for (int i=0; i<getPartitions(); i++) {
                    props[i] = new Properties();
                    props[i].setProperty("start", String.valueOf(i*10+1));
                    props[i].setProperty("end", String.valueOf((i+1)*10));
                }
                return props;
            }
        };
    }
}
