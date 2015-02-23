package org.javaee7.extra.nosql.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author Arun Gupta
 */
@Named
@Stateless
public class PersonSessionBean {

    @Inject
    Person person;

    Set<String> set = new HashSet<>();

    private static final String personsColumnFamily = "person";
    private static final String personsTable = "persons";
    HTablePool pool;

    @PostConstruct
    private void initDB() {
        try {
            // By default, it's localhost, don't worry.
            Configuration config = HBaseConfiguration.create();

            //            HTable table = new HTable(config, personsTable);

            HBaseAdmin admin = new HBaseAdmin(config);
            HTableDescriptor blogstable = new HTableDescriptor(personsTable);
            admin.createTable(blogstable);
            //
            //            // Cannot edit a stucture on an active table.
            //            admin.disableTable(personsTable);
            //
            //            HColumnDescriptor userCol = new HColumnDescriptor("name");
            //            admin.addColumn(personsTable, userCol);
            //
            //            HColumnDescriptor ageCol = new HColumnDescriptor("age");
            //            admin.addColumn(personsTable, ageCol);
            //
            //            // For readin, it needs to be re-enabled.
            //            admin.enableTable(personsTable);
        } catch (IOException ex) {
            Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    private void stopDB() {
    }

    public void createPerson() throws IOException {
        try (HTableInterface table = pool.getTable(personsTable)) {

            Put put = new Put(Bytes.toBytes(person.getName()), Calendar.getInstance().getTime().getTime());
            put.add(Bytes.toBytes(personsColumnFamily),
                Bytes.toBytes("name"),
                Calendar.getInstance().getTime().getTime(),
                Bytes.toBytes(person.getName()));
            put.add(Bytes.toBytes(personsColumnFamily),
                Bytes.toBytes("age"),
                Calendar.getInstance().getTime().getTime(),
                Bytes.toBytes(person.getAge()));
            table.put(put);
        }
    }

    public List<Person> getPersons() throws IOException {
        List<Person> persons = new ArrayList<>();

        try (HTableInterface table = pool.getTable(personsTable)) {
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes(personsColumnFamily));
            try (ResultScanner resultScanner = table.getScanner(scan)) {
                for (Result result : resultScanner) {
                    for (KeyValue kv : result.raw()) {
                        Person p = new Person();
                        //                    p.setTitle(Bytes.toString(kv.getQualifier()));
                        //                    p.setBody(Bytes.toString(kv.getValue()));
                        //                    p.setId(Bytes.toString(result.getRow()));
                        persons.add(person);
                    }
                }
            }
        }

        return persons;
    }
}
