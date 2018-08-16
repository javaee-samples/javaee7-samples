/** Copyright Payara Services Limited **/
package org.javaee7.servlet.security.digest;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;


/**
 * 
 * @author Arjan Tijms
 *
 */
@WebListener
public class DatabaseSetup implements ServletContextListener {
    
    @Resource
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Creating DB tables");
        
        // Note "eesamplesdigestrealm" is the name of the realm as defined in web.xml:
        //
        //<login-config>
        //    <auth-method>DIGEST</auth-method>
        //    <realm-name>eesamplesdigestrealm</realm-name>
        //</login-config>
        
        String ha1 = md5Hex("u1" + ":" + "eesamplesdigestrealm" + ":" + "p1");
        
        System.out.println("ha1=" + ha1);
        
        tryDropTables();
        
        System.out.println("Adding user u1 with group g1 to database");
        
        execute(dataSource, "CREATE TABLE usertable(username VARCHAR(32) PRIMARY KEY, password VARCHAR(127))");
        execute(dataSource, "CREATE TABLE grouptable(username VARCHAR(64), groupname VARCHAR(64))");
        
        execute(dataSource, "INSERT INTO usertable VALUES('u1', '" + ha1 + "')");
        
        execute(dataSource, "INSERT INTO grouptable VALUES('u1', 'g1')");
        
        System.out.println("Done creating DB tables");
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        tryDropTables();
    }
    
    private void execute(DataSource dataSource, String query) {
        try (
            Connection connection = dataSource.getConnection(); 
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.executeUpdate();
        } catch (SQLException e) {
           throw new IllegalStateException(e);
        }
    }
    
    private void tryDropTables() {
        try {
            execute(dataSource, "DROP TABLE IF EXISTS usertable");
            execute(dataSource, "DROP TABLE IF EXISTS grouptable");
        } catch (Exception e) {
            try {
                execute(dataSource, "DROP TABLE usertable");
                execute(dataSource, "DROP TABLE grouptable");
            } catch (Exception ee) {
            }
        }
        
    }
    
}
