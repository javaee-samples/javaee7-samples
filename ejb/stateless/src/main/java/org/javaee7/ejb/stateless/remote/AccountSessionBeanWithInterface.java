package org.javaee7.ejb.stateless.remote;

import javax.ejb.Stateless;

/**
 * @author Arun Gupta
 */
@Stateless
public class AccountSessionBeanWithInterface implements Account {

    @Override
    public String withdraw(float amount) {
        return "Withdrawn: " + amount;
    }

    @Override
    public String deposit(float amount) {
        return "Deposited: " + amount;
    }
}
