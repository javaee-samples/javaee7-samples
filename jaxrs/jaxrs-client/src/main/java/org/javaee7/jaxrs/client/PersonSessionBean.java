package org.javaee7.jaxrs.client;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Singleton;

/**
 * @author Arun Gupta
 */
@Singleton
public class PersonSessionBean {
    private final List<Person> list;

    public PersonSessionBean() {
        list = new ArrayList<>();
    }

    public void addPerson(Person p) {
        list.add(p);
    }

    public void deletePerson(String name) {
        Person p = findPersonByName(name);
        if (p != null)
            list.remove(p);
    }

    private Person findPersonByName(String name) {
        for (Person p : list) {
            if (name.equals(p.getName()))
                return p;
        }
        return null;
    }

    public List<Person> getPersons() {
        return list;
    }
}
