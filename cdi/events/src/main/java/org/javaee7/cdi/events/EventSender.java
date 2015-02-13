package org.javaee7.cdi.events;

/**
 * @author Radim Hanus
 */
public interface EventSender {
    void send(String message);
}
