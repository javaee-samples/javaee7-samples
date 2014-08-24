package org.javaee7.servlet.event.listeners;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.annotation.WebListener;

/**
 * Web application lifecycle listener.
 *
 * @author Arun Gupta
 */
@WebListener
public class MyContextAttributeListener implements ServletContextAttributeListener {

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        System.out.println("MyContextAttributeListener.attributeAdded: " + event.getName());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        System.out.println("MyContextAttributeListener.attributeRemoved: " + event.getName());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        System.out.println("MyContextAttributeListener.attributeReplaced: " + event.getName());
    }
}
