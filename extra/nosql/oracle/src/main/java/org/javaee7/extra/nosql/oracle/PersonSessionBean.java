package org.javaee7.extra.nosql.oracle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.Key;
import oracle.kv.Value;

/**
 * @author Arun Gupta
 */
@Named
@Singleton
public class PersonSessionBean {

    @Inject
    Person person;

    Set<String> set = new HashSet<>();

    private KVStore store;

    @PostConstruct
    private void initDB() {
        // bootstrap
        store = KVStoreFactory.getStore(new KVStoreConfig("kvstore", "localhost:5000"));
    }

    @PreDestroy
    private void stopDB() {
    }

    public void createPerson() {
        store.put(Key.createKey(person.getName()), Value.createValue(new Person(person.getName(), person.getAge()).toString().getBytes()));
        set.add(person.getName());
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList();
        for (String key : set) {
            persons.add(Person.fromString(new String(store.get(Key.createKey(key)).getValue().getValue())));
        }
        return persons;
    }
}
