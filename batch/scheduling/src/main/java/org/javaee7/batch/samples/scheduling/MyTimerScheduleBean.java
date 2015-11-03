package org.javaee7.batch.samples.scheduling;

import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author arungupta
 */
@Startup
@Singleton
public class MyTimerScheduleBean extends AbstractTimerBatch {
}
