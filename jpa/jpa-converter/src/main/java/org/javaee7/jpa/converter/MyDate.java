/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jpa.converter;

import java.util.Date;

/**
 * @author Arun Gupta
 */
public class MyDate {
    Date date;

    public MyDate() {
    }

    public MyDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    
}
