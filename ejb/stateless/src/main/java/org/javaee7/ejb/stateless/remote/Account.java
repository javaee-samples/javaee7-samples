package org.javaee7.ejb.stateless.remote;

import javax.ejb.Remote;

/**
 * @author Arun Gupta
 */
@Remote
public interface Account {

    public String withdraw(float amount);

    public String deposit(float amount);

}
