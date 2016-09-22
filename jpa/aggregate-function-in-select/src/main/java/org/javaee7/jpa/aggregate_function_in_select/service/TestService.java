package org.javaee7.jpa.aggregate_function_in_select.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.javaee7.jpa.aggregate_function_in_select.entity.AggregatedTestEntity;
import org.javaee7.jpa.aggregate_function_in_select.entity.TestEntity;

/**
 * This service contains methods to insert two test entities into the database
 * and to get the aggregation of those.
 * 
 * @author Arjan Tijms
 *
 */
@Stateless
public class TestService {

    @PersistenceContext
    private EntityManager entityManager;

    public void saveEntities() {

        TestEntity testEntity1 = new TestEntity();
        testEntity1.setValue("1");
        TestEntity testEntity2 = new TestEntity();
        testEntity2.setValue("2");

        entityManager.persist(testEntity1);
        entityManager.persist(testEntity2);
    }

    public List<AggregatedTestEntity> getAggregation() {
        return entityManager
            .createQuery(
                // Note that GROUP_CONCAT is a DB specific function, in this
                // case for HSQLDB.
                "SELECT new org.javaee7.jpa.aggregate_function_in_select.entity.AggregatedTestEntity("
                +    "FUNCTION('GROUP_CONCAT', _testEntity.value)" 
                +") " 
                +"FROM "
                +    "TestEntity _testEntity ",
            AggregatedTestEntity.class
            ).getResultList();
    }

}
