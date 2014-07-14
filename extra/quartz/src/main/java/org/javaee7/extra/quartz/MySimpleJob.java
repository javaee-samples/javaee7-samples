package org.javaee7.extra.quartz;

import java.util.Calendar;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Arun Gupta
 */
public class MySimpleJob implements Job {

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        System.out.println("MySimpleJob.execute: " + Calendar.getInstance().getTime());
    }
    
}
