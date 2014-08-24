package org.javaee7.jsf.server.extension;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@ApplicationScoped
public class User {
    private int age;
    private String name;
    
    public User() { }
    
    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
