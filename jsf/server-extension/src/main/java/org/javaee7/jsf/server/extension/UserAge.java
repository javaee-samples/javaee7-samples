package org.javaee7.jsf.server.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@ApplicationScoped
public class UserAge {
    int age;

    public UserAge() { }

    public UserAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
