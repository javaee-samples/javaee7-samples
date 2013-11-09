/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Arun Gupta
 */
@Converter
public class CreditCardConverter implements AttributeConverter<CreditCard, String> {

    @Override
    public String convertToDatabaseColumn(CreditCard attribute) {
        return attribute.toString();
    }

    @Override
    public CreditCard convertToEntityAttribute(String card) {
        return new CreditCard(card);
    }
    
}
