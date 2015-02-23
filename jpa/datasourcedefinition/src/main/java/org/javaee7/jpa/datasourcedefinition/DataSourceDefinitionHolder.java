package org.javaee7.jpa.datasourcedefinition;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;

/**
 * @author Alexis Hassler
 */
@DataSourceDefinition(name = "java:global/MyApp/MyDataSource",
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:mem:test")
@Stateless
public class DataSourceDefinitionHolder {
}
