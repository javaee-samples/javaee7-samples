package org.javaee7.jaxrs.mapping.exceptions;

/**
 * @author Arun Gupta
 */
public class OrderNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>OrderNotFoundException</code> without
     * detail message.
     */
    public OrderNotFoundException() {
    }

    public OrderNotFoundException(int id) {
        super("Order not found: " + id);
    }

    /**
     * Constructs an instance of <code>OrderNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
