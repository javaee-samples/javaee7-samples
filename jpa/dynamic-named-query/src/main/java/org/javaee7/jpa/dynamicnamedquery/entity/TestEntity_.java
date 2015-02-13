package org.javaee7.jpa.dynamicnamedquery.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * The meta model of our JPA entity, which we can use to refer to the Entity's attributes
 * in a type-safe way (if only Java 8 had also introduced attribute/property references...)
 * 
 * @author Arjan Tijms
 *
 */
@StaticMetamodel(TestEntity.class)
public class TestEntity_ {

    public static volatile SingularAttribute<TestEntity, Long> id;
    public static volatile SingularAttribute<TestEntity, String> value;

}
