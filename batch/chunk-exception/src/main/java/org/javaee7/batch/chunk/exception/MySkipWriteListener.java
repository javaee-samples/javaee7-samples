package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.listener.SkipWriteListener;
import javax.inject.Named;
import java.util.List;

/**
 * @author Arun Gupta
 */
@Named
public class MySkipWriteListener implements SkipWriteListener {
    @Override
    public void onSkipWriteItem(List list, Exception e) throws Exception {
        ChunkExceptionRecorder.chunkExceptionsCountDownLatch.countDown();
        System.out.println("MySkipWriteListener.onSkipWriteItem: " + list.size() + ", " + e.getMessage());
        list.remove(new MyOutputRecord(2));
    }
}
