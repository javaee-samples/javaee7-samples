package org.samples.wildfly.scheduling.batch;

import java.util.Properties;
import javax.batch.runtime.BatchRuntime;

/**
 * @author arungupta
 */
public class MyJob implements Runnable {

    public void run() {
        BatchRuntime.getJobOperator().start("myJob", new Properties());
    }
    
}
