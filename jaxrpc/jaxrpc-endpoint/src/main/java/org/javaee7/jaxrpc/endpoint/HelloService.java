package org.javaee7.jaxrpc.endpoint;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote {
    String sayHello(String s) throws RemoteException;
}