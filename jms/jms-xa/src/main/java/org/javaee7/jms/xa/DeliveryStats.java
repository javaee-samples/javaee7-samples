package org.javaee7.jms.xa;

import java.util.concurrent.CountDownLatch;

import javax.ejb.Singleton;

@Singleton
public class DeliveryStats {
	
	public static CountDownLatch countDownLatch = new CountDownLatch(1);

    private long deliveredMessagesCount;

    public long getDeliveredMessagesCount() {
        return deliveredMessagesCount;
    }

    public void messageDelivered() {
        deliveredMessagesCount++;
    }

    public void reset() {
        deliveredMessagesCount = 0L;
    }
}
