package org.javaee7.extra.nosql.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@Stateless
public class PersonSessionBean {

    @Inject
    Person person;

    DBCollection personCollection;

    @PostConstruct
    private void initDB() {
        try {                        
            // Get an instance of Mongo
            Mongo m = new Mongo("localhost", 27017);
            DB db = m.getDB("personDB");
            personCollection = db.getCollection("persons");
            if (personCollection == null) {
                personCollection = db.createCollection("persons", null);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void createPerson() {
        BasicDBObject doc = person.toDBObject();
        personCollection.insert(doc);
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList();
        DBCursor cur = personCollection.find();
        System.out.println("getPersons: Found " + cur.length() + " person(s)");
        for (DBObject dbo : cur.toArray()) {
            persons.add(Person.fromDBObject(dbo));
        }

        return persons;
    }
}
