package org.javaee7.jaxrs.paramconverter;

/**
 * @author Xavier Coulon
 *
 */
public class MyBean {

    private String value;

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getValue();
    }

}
