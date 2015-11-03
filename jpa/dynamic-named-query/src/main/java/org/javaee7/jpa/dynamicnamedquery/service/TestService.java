package org.javaee7.jpa.dynamicnamedquery.service;

import static org.javaee7.jpa.dynamicnamedquery.service.QueryRepository.Queries.TEST_ENTITY_GET_ALL;
import static org.javaee7.jpa.dynamicnamedquery.service.QueryRepository.Queries.TEST_ENTITY_GET_BY_VALUE;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.javaee7.jpa.dynamicnamedquery.entity.TestEntity;
import org.javaee7.jpa.dynamicnamedquery.entity.TestEntity_;

/**
 * 
 * @author Arjan Tijms
 *
 */
@Stateless
public class TestService {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(TestEntity testEntity) {
        entityManager.persist(testEntity);
    }

    /**
     * Gets a list of all instances of {@link TestEntity} that were persisted
     * 
     * @return a list of all instances of {@link TestEntity} that were persisted
     */
    public List<TestEntity> getAll() {
        return entityManager.createNamedQuery(TEST_ENTITY_GET_ALL.name(), TestEntity.class).getResultList();
    }

    /**
     * Gets a list of instances of {@link TestEntity} where the <code>value</code> attribute equals
     * the <code>value</code> parameter of this method.
     * 
     * @param value the value by which {@link TestEntity} instances are retrieved.
     * @return list of {@link TestEntity} instances matching <code>value</code>
     */
    public List<TestEntity> getByValue(String value) {
        return entityManager.createNamedQuery(TEST_ENTITY_GET_BY_VALUE.name(), TestEntity.class)
            .setParameter(TestEntity_.value.getName(), value).getResultList();
    }

}
