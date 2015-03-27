package org.javaee7.ejb.timer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * @author Jacek Jackowiak
 */
@Startup
@Singleton
public class MultipleScheduleTimerBean {

    @Inject
    Event<Ping> pingEvent;

    @Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 second timer")
    public void automaticallyScheduled(Timer timer) {
        fireEvent(timer);
    }

    @Schedule(hour = "*", minute = "*", second = "*/10", info = "Every 10 second timer")
    public void automaticallyScheduled2(Timer timer) {
        fireEvent(timer);
    }

    private void fireEvent(Timer timer) {
        pingEvent.fire(new Ping(timer.getInfo().toString()));
    }
}
