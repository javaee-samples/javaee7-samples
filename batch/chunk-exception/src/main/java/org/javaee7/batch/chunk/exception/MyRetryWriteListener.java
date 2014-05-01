package org.javaee7.batch.chunk.exception;

import javax.batch.api.chunk.listener.RetryWriteListener;
import javax.inject.Named;
import java.util.List;

/**
 * @author Roberto Cortez
 */
@Named
public class MyRetryWriteListener implements RetryWriteListener {
    @Override
    public void onRetryWriteException(List<Object> items, Exception ex) throws Exception {
        System.out.println("MyRetryWriteListener.onRetryWriteException");
    }
}
