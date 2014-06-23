package org.javaee7.batch.chunk.checkpoint;

import javax.batch.api.chunk.AbstractCheckpointAlgorithm;
import javax.inject.Named;
import java.util.concurrent.CountDownLatch;

/**
 * @author Arun Gupta
 */
@Named
public class MyCheckpointAlgorithm extends AbstractCheckpointAlgorithm {
    public static CountDownLatch checkpointCountDownLatch = new CountDownLatch(10);

    @Override
    public boolean isReadyToCheckpoint() throws Exception {
        checkpointCountDownLatch.countDown();
        return MyItemReader.COUNT % 5 == 0;
    }
}
