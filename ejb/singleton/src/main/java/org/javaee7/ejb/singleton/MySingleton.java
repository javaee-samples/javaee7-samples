package org.javaee7.ejb.singleton;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author Arun Gupta
 */
@Startup
@Singleton
public class MySingleton {
    StringBuilder builder;

    @PostConstruct
    private void postConstruct() {
        System.out.println("postConstruct");
        builder = new StringBuilder();
    }

    @Lock(LockType.READ)
    public String readSomething() {
        return "current timestamp: " + new Date();
    }

    @Lock(LockType.WRITE)
    public String writeSomething(String something) {
        builder.append(something);
        return builder.toString() + " : " + new Date();
    }
}
