package org.javaee7.extra.nosql.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import javax.enterprise.inject.Model;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
@Model
public class Person {

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

    public BasicDBObject toDBObject() {
        BasicDBObject doc = new BasicDBObject();

        doc.put("name", name);
        doc.put("age", age);

        return doc;
    }

    public static Person fromDBObject(DBObject doc) {
        Person p = new Person();

        p.name = (String) doc.get("name");
        p.age = (int) doc.get("age");

        return p;
    }

    @Override
    public String toString() {
        return name + ", " + age;
    }
}
