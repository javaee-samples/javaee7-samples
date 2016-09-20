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
