/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jpa.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Arun Gupta
 */
@Converter
public class EmployeeDateConverter implements AttributeConverter<MyDate, String> {
    final SimpleDateFormat format = new SimpleDateFormat("M/dd/yy");

    @Override
    public String convertToDatabaseColumn(MyDate attribute) {
        return attribute.getDate().toString();
    }

    @Override
    public MyDate convertToEntityAttribute(String date) {
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return new MyDate(d);
    }
    
}
