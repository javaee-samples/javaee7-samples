package org.javaee7.batch.samples.scheduling;

import java.util.concurrent.CountDownLatch;

import javax.batch.api.listener.AbstractStepListener;
import javax.inject.Named;

@Named
public class MyStepListener extends AbstractStepListener {
    
    public static CountDownLatch countDownLatch = new CountDownLatch(3);

    @Override
    public void beforeStep() throws Exception {
    }

    @Override
    public void afterStep() throws Exception {
        countDownLatch.countDown();
    }
}
