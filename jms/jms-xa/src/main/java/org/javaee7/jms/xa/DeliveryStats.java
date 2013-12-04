package org.javaee7.jms.xa;

import javax.ejb.Singleton;

@Singleton
public class DeliveryStats {

    private long deliveredMessagesCount;

    public long getDeliveredMessagesCount()
    {
        return deliveredMessagesCount;
    }

    public void messageDelivered()
    {
        deliveredMessagesCount++;
    }

    public void reset()
    {
        deliveredMessagesCount = 0L;
    }
}
