package org.javaee7.extra.nosql.riak;

import java.io.Serializable;
import java.util.StringTokenizer;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
@Named
@ApplicationScoped
public class Person implements Serializable {
    
    @Size(min = 1, max = 20)
    private String name;

    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return name + ", " + age;
    }
    
    public static Person fromString(String string) {
        StringTokenizer tokens = new StringTokenizer(string, ",");
        
        return new Person(tokens.nextToken(), Integer.parseInt(tokens.nextToken().trim()));
    }
    
}
