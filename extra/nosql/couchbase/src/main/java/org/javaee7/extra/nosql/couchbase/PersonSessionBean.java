package org.javaee7.extra.nosql.couchbase;

import com.couchbase.client.CouchbaseClient;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author Arun Gupta
 */
@Named
@Singleton
public class PersonSessionBean {

    @Inject
    Person person;
    
    CouchbaseClient client;
    
    Set<String> set = new HashSet<>();

    @PostConstruct
    private void initDB() {
        try {
            // Get an instance of Couchbase
            List<URI> hosts = Arrays.asList(
                    new URI("http://localhost:8091/pools")
            );

            // Get an instance of Couchbase
            // Name of the Bucket to connect to
            String bucket = "default";

            // Password of the bucket (empty) string if none
            String password = "";

            // Connect to the Cluster
            client = new CouchbaseClient(hosts, bucket, password);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    private void stopDB() {
        client.shutdown();
    }

    public void createPerson() {
        client.set(person.getName(), new Person(person.getName(), person.getAge()));
        set.add(person.getName());
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList();
        Map<String, Object> map = client.getBulk(set.iterator());
        for (String key : map.keySet()) {
            persons.add((Person)map.get(key));
        }
        return persons;
    }
}
