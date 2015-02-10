package org.javaee7.jpa.datasourcedefinition_annotation_pu.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;

@DataSourceDefinition(
    name = "java:app/MyApp/MyDS",
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:mem:test")
@Stateless
public class DataSourceDefinitionConfig {
}
