/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jpa.converter;

import java.io.Serializable;

/**
 * @author Arun Gupta
 */
public class CreditCard implements Serializable {
    String cardNumber;

    public CreditCard() {
    }

    public CreditCard(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return cardNumber;
    }

    
}
