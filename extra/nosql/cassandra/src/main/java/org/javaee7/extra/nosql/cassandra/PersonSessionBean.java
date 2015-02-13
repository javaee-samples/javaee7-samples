package org.javaee7.extra.nosql.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@Singleton
public class PersonSessionBean {

    @Inject
    Person person;

    private Cluster cluster;
    private Session session;

    private PreparedStatement selectAllPersons;
    private PreparedStatement insertPerson;

    @PostConstruct
    private void initDB() {
        cluster = Cluster.builder()
            .addContactPoint("localhost")
            // .withSSL() // Uncomment if using client to node encryption
            .build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
                host.getDatacenter(), host.getAddress(), host.getRack());
        }
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS test WITH replication "
            + "= {'class':'SimpleStrategy', 'replication_factor':1};");

        session.execute(
            "CREATE TABLE IF NOT EXISTS test.person ("
                + "name text PRIMARY KEY,"
                + "age int"
                + ");");

        selectAllPersons = session.prepare("SELECT * FROM test.person");
        insertPerson = session.prepare(
            "INSERT INTO test.person (name, age) VALUES (?, ?);"
            );
    }

    @PreDestroy
    private void stopDB() {
        cluster.shutdown();
    }

    public void createPerson() {
        session.execute(insertPerson.bind(person.getName(), person.getAge()));
    }

    public List<Person> getPersons() {
        List<Person> persons = new ArrayList<>();
        ResultSet results = session.execute(selectAllPersons.bind());
        for (Row row : results) {
            persons.add(new Person(row.getString("name"), row.getInt("age")));
        }
        return persons;
    }
}
