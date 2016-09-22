/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
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
