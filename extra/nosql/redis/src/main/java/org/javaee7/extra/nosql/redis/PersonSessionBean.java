package org.javaee7.extra.nosql.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import redis.clients.jedis.Jedis;

/**
 * @author Arun Gupta
 */
@Named
@Stateless
public class PersonSessionBean {

    @Inject
    Person person;
    
    Jedis jedis;
    
    Set<String> set = new HashSet<>();

    @PostConstruct
    private void initDB() {
//         Start embedded Redis
        jedis = new Jedis("localhost", 6379);
    }
    
    @PreDestroy
    private void stopDB() {
        jedis.shutdown();
    }

    public void createPerson() {
        jedis.set(person.getName(), new Person(person.getName(), person.getAge()).toString());
        set.add(person.getName());
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        for (String key : set) {
            persons.add(Person.fromString(jedis.get(key)));
        }
        return persons;
    }
}
