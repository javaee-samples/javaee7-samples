package org.javaee7.servlet.event.listeners;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * Web application lifecycle listener.
 *
 * @author Arun Gupta
 */
public class MyHttpSessionBindingListener implements HttpSessionBindingListener {

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        System.out.println("MyHttpSessionBindingListener.valueBound: " + event.getName());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        System.out.println("MyHttpSessionBindingListener.valueUnbound: " + event.getName());
    }

}
