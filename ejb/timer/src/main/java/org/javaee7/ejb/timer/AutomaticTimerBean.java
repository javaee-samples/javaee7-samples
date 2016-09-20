package org.javaee7.ejb.timer;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timer;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Collection;

/**
 * @author Arun Gupta
 */
@Startup
@Singleton
public class AutomaticTimerBean {

    @Resource
    SessionContext ctx;

    @Inject
    Event<Ping> pingEvent;

    @Schedule(hour = "*", minute = "*", second = "*/5", info = "Every 5 second timer")
    public void printDate() {
        Collection<Timer> timers = ctx.getTimerService().getAllTimers();
        for (Timer t : timers) {
            pingEvent.fire(new Ping(t.getInfo().toString()));
        }
    }

}
