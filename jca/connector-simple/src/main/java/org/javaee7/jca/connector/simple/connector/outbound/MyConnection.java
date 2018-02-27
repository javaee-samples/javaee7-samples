package org.javaee7.jca.connector.simple.connector.outbound;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.ResultSetInfo;

/**
 *
 * @author arungup
 */
public class MyConnection implements Connection {

    private final MyManagedConnection myManagedConnection;

    public MyConnection(MyManagedConnection myManagedConnection) {
        this.myManagedConnection = myManagedConnection;
    }

    @Override
    public Interaction createInteraction() throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ConnectionMetaData getMetaData() throws ResourceException {
        return new ConnectionMetaData() {
            @Override
            public String getEISProductName() {
                return "My Connector Platform";
            }

            @Override
            public String getEISProductVersion() {
                return "1.0";
            }

            @Override
            public String getUserName() {
                return "myUser";
            }
        };

    }

    @Override
    public ResultSetInfo getResultSetInfo() throws ResourceException {
        return null;
    }

    @Override
    public void close() throws ResourceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
