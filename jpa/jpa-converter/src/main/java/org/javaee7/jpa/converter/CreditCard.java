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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CreditCard)) {
            return false;
        }

        final CreditCard that = (CreditCard) o;

        if (cardNumber != null ? !cardNumber.equals(that.cardNumber) : that.cardNumber != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cardNumber != null ? cardNumber.hashCode() : 0;
    }
}
