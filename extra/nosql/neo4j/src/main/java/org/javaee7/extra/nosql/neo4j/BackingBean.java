package org.javaee7.extra.nosql.neo4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
@Named
@ApplicationScoped
public class BackingBean {

    @Size(min = 1, max = 20)
    private String name;

    private int age;

    private String name2;

    private int age2;

    private String relationship;

    public BackingBean() {
    }

    public BackingBean(String name, int age) {
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

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String person1String() {
        return name + ", " + age;
    }

    public String person2String() {
        return name2 + ", " + age2;
    }
}
