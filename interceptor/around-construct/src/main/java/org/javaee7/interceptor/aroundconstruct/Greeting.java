package org.javaee7.interceptor.aroundconstruct;

/**
 * @author Radim Hanus
 */
public interface Greeting {
    boolean isConstructed();

    boolean isInitialized();

    Param getParam();
}
