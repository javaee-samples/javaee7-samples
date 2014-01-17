package org.javaee7.jms.batch;

import javax.ejb.Singleton;

/**
 * @author Patrik Dudits
 */
@Singleton
public class ResultCollector {

    private int numberOfJobs;
    private int lastItemCount;
    private int lastSum;

    public void postResult(int sum, int numItems) {
        numberOfJobs++;
        lastItemCount = numItems;
        lastSum = sum;
    }

    public int getNumberOfJobs() {
        return numberOfJobs;
    }

    public int getLastItemCount() {
        return lastItemCount;
    }

    public int getLastSum() {
        return lastSum;
    }
}
