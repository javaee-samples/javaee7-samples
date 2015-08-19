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
package org.javaee7.extra.nosql.neo4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * @author Arun Gupta
 */
@Named
@Singleton
public class PersonSessionBean {

    @Inject
    BackingBean backingBean;

    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;

    private static enum RelTypes implements RelationshipType {
        SPOUSE,
        BROTHER,
        SISTER
    }

    Set<String> set = new HashSet<>();

    @PostConstruct
    private void initDB() {
        try {
            Path tempDir = Files.createTempDirectory("test-neo4j");
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(tempDir.toString());
            try (Transaction tx = graphDb.beginTx()) {
                firstNode = graphDb.createNode();
                secondNode = graphDb.createNode();
                tx.success();
            }
        } catch (IOException ex) {
            Logger.getLogger(PersonSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    private void stopDB() {
        graphDb.shutdown();
    }

    public void createPerson() {
        try (Transaction tx = graphDb.beginTx()) {
            firstNode.setProperty(backingBean.getName(), backingBean.person1String());
            secondNode.setProperty(backingBean.getName2(), backingBean.person2String());
            switch (backingBean.getRelationship()) {
                case "spouse":
                    firstNode.createRelationshipTo(secondNode, RelTypes.SPOUSE);
                    break;
                case "brother":
                    firstNode.createRelationshipTo(secondNode, RelTypes.BROTHER);
                    break;
                case "sister":
                    firstNode.createRelationshipTo(secondNode, RelTypes.SISTER);
                    break;
            }
            tx.success();
        }
    }

    public List<BackingBean> getPersons() {
        List<BackingBean> beans = new ArrayList();
        try (Transaction tx = graphDb.beginTx()) {
            for (String key : firstNode.getPropertyKeys()) {
                BackingBean bean = new BackingBean();
                Person p = Person.fromString((String) firstNode.getProperty(key));
                bean.setName(p.getName());
                bean.setAge(p.getAge());
                for (Relationship r : firstNode.getRelationships(RelTypes.SPOUSE, RelTypes.SISTER, RelTypes.BROTHER)) {
                    if (r.isType(RelTypes.SPOUSE)) {
                        bean.setRelationship("spouse");
                        break;
                    } else if (r.isType(RelTypes.SISTER)) {
                        bean.setRelationship("sister");
                        break;
                    } else if (r.isType(RelTypes.BROTHER)) {
                        bean.setRelationship("brother");
                        break;
                    }
                }
                beans.add(bean);
            }
            tx.success();
        }
        return beans;
    }
}
