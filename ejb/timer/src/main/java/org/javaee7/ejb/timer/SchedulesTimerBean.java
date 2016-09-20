package org.javaee7.ejb.timer;

import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * @author Jacek Jackowiak
 */
@Startup
@Singleton
public class SchedulesTimerBean {

    @Inject
    Event<Ping> pingEvent;

    @Schedules({
            @Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 second timer"),
            @Schedule(hour = "*", minute = "*", second = "*/10", info = "Every 10 second timer")
    })
    public void automaticallyScheduled(Timer timer) {
        pingEvent.fire(new Ping(timer.getInfo().toString()));
    }

}
