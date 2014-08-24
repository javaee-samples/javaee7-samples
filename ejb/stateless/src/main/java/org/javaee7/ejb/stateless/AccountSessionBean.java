package org.javaee7.ejb.stateless;

import javax.ejb.Stateless;

/**
 * @author Arun Gupta
 */
@Stateless
public class AccountSessionBean {

    private float amount = 0;

    public String withdraw(float amount) {
        this.amount -= amount;
        return "Withdrawn: " + amount;
    }

    public String deposit(float amount) {
        this.amount += amount;
        return "Deposited: " + amount;
    }

    public float getAmount() {
        return this.amount;
    }
}
