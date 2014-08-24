package org.javaee7.servlet.event.listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;

/**
 * Web application lifecycle listener.
 *
 * @author Arun Gupta
 */
@WebListener
public class MySessionIdListener implements HttpSessionIdListener {

    @Override
    public void sessionIdChanged(HttpSessionEvent event, String oldSessionId) {
        System.out.println("MySessionIdListener.sessionIdChanged: new=" + event.getSession().getId() + ", old=" + oldSessionId);
    }

}
