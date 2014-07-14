package org.javaee7.jaxrs.moxy;

/**
 * @author Arun Gupta
 */
public class MyObject {

    private String name;
    private int age;

    public MyObject() {
    }

    public MyObject(String name, int age) {
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
}
