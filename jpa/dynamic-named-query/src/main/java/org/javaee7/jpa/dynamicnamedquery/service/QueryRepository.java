package org.javaee7.jpa.dynamicnamedquery.service;

import static org.javaee7.jpa.dynamicnamedquery.service.QueryRepository.Queries.TEST_ENTITY_GET_ALL;
import static org.javaee7.jpa.dynamicnamedquery.service.QueryRepository.Queries.TEST_ENTITY_GET_BY_VALUE;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.javaee7.jpa.dynamicnamedquery.entity.TestEntity;
import org.javaee7.jpa.dynamicnamedquery.entity.TestEntity_;

/**
 * This bean's init method is called when the AS starts and dynamically creates a number of queries
 * and sets them as named queries on the entity manager factory.
 * <p>
 * Dynamically adding named queries is a new feature in Java EE 7.
 * 
 * @author Arjan Tijms
 *
 */
@Singleton
@Startup
public class QueryRepository {

    public enum Queries {
        TEST_ENTITY_GET_ALL,
        TEST_ENTITY_GET_BY_VALUE
    }

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {

        // Stores queries that were created via the Criteria API as named queries.

        // This is the Criteria alternative for the feature where JPQL queries can
        // be placed in orm.xml files or annotations. (but note that JPQL queries can also
        // be added here programmatically).

        entityManagerFactory.addNamedQuery(TEST_ENTITY_GET_ALL.name(), buildGetAll());
        entityManagerFactory.addNamedQuery(TEST_ENTITY_GET_BY_VALUE.name(), buildGetByValue());
    }

    /**
     * Builds a criteria query equal to the JPQL
     * 
     * <code>SELECT _testEntity FROM TestEntity _testEntity</code>
     * 
     *
     */
    private TypedQuery<TestEntity> buildGetAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TestEntity> criteriaQuery = criteriaBuilder.createQuery(TestEntity.class);
        Root<TestEntity> root = criteriaQuery.from(TestEntity.class);

        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery);
    }

    /**
     * Builds a criteria query equal to the JPQL
     * 
     * <code>SELECT _testEntity FROM TestEntity _testEntity WHERE _testEntity.value :value</code>
     * 
     *
     */
    private TypedQuery<TestEntity> buildGetByValue() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<TestEntity> criteriaQuery = criteriaBuilder.createQuery(TestEntity.class);
        Root<TestEntity> root = criteriaQuery.from(TestEntity.class);
        ParameterExpression<String> valueParameter = criteriaBuilder.parameter(String.class, TestEntity_.value.getName());

        criteriaQuery.select(root)
            .where(
                criteriaBuilder.equal(
                    root.get(TestEntity_.value), valueParameter)
            );

        return entityManager.createQuery(criteriaQuery);
    }

}
