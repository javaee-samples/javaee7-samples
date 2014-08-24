package org.javaee7.servlet.event.listeners;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * Web application lifecycle listener.
 *
 * @author Arun Gupta
 */
@WebListener
public class MyHttpSessionAttributeListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        System.out.println("MyHttpSessionAttributeListener.attributeAdded: " + event.getName());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        System.out.println("MyHttpSessionAttributeListener.attributeRemoved: " + event.getName());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        System.out.println("MyHttpSessionAttributeListener.attributeReplaced: " + event.getName());
    }
}
