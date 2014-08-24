package org.javaee7.jaxrs.readerwriter.injection;

import java.io.Serializable;

/**
 * @author Arun Gupta
 */
public class MyObject implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String MIME_TYPE = "application/myType";

    private int index;

    public MyObject() {
    }

    public MyObject(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
