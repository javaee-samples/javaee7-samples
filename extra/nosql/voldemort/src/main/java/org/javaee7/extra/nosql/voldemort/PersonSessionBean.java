package org.javaee7.extra.nosql.voldemort;

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
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.versioning.Versioned;

/**
 * @author Arun Gupta
 */
@Named
@Singleton
public class PersonSessionBean {

    @Inject
    Person person;

    Set<String> set = new HashSet<>();

    StoreClient client;

    @PostConstruct
    private void initDB() {
        //        // start embedded
        //        VoldemortConfig config = VoldemortConfig.loadFromEnvironmentVariable();
        //        VoldemortServer server = new VoldemortServer(config);
        //        server.start();

        // bootstrap
        String bootstrapUrl = "tcp://localhost:6666";
        StoreClientFactory factory = new SocketStoreClientFactory(new ClientConfig().setBootstrapUrls(bootstrapUrl));

        // create a client that executes operations on a single store
        client = factory.getStoreClient("test");
    }

    @PreDestroy
    private void stopDB() {
    }

    public void createPerson() {
        client.put(person.getName(), new Person(person.getName(), person.getAge()).toString());
        set.add(person.getName());
    }

    public List<Person> getPersons() {
        Map<String, Versioned> map = client.getAll(set);
        List<Person> persons = new ArrayList();
        for (String key : map.keySet()) {
            persons.add(Person.fromString(map.get(key).getValue().toString()));
        }
        return persons;
    }
}
