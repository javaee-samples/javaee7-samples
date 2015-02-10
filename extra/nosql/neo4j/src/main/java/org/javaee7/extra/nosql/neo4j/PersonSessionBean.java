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
