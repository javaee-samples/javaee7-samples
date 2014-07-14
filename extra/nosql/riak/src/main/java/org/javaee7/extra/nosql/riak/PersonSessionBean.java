package org.javaee7.extra.nosql.riak;

import com.basho.riak.client.IRiakClient;
import com.basho.riak.client.RiakException;
import com.basho.riak.client.RiakFactory;
import com.basho.riak.client.RiakRetryFailedException;
import com.basho.riak.client.bucket.Bucket;
import com.basho.riak.client.cap.UnresolvedConflictException;
import com.basho.riak.client.convert.ConversionException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    Set<String> set = new HashSet<>();
    Bucket myBucket;

    @PostConstruct
    private void initDB() {
        try {
            IRiakClient client = RiakFactory.pbcClient("localhost", 8087);
            myBucket = client.fetchBucket("test").execute();
        } catch (RiakException ex) {
            Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    private void stopDB() {
    }

    public void createPerson() {
        try {
            myBucket.store(person.getName(), new Person(person.getName(), person.getAge())).execute();
            set.add(person.getName());
        } catch (RiakRetryFailedException | UnresolvedConflictException | ConversionException ex) {
            Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList();
        for (String key : set) {
            try {
                Person p = myBucket.fetch(key, Person.class).execute();
                persons.add(p);
            } catch (UnresolvedConflictException | RiakRetryFailedException | ConversionException ex) {
                Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return persons;
    }
}
