package org.javaee7.cdi.interceptors;

/**
 * @author Arun Gupta
 * @author Radim Hanus
 */
@MyInterceptorBinding
public class SimpleGreeting implements Greeting {
    private String greet;

    public String getGreet() {
        return greet;
    }

    public void setGreet(String greet) {
        this.greet = greet;
    }
}
