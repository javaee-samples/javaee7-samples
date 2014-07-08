package org.samples.wildfly.scheduling.batch;

import java.util.Properties;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 * @author arungupta
 */
@Singleton
public class MySingletonEJB {
    @Schedule(hour = "23", minute = "59", second = "59")
    public void myJob() {
        BatchRuntime.getJobOperator().start("myJob", new Properties());
    }
}
