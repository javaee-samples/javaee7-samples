package org.javaee7.ejb.stateful.remote;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 * @author Arun Gupta
 */
@Stateful
public class CartBeanWithInterface implements Cart {

    List<String> items;

    public CartBeanWithInterface() {
        items = new ArrayList<>();
    }

    @Override
    public void addItem(String item) {
        items.add(item);
    }

    @Override
    public void removeItem(String item) {
        items.remove(item);
    }

    @Override
    public void purchase() { 
        //. . .
    }
    
    @Override
    public List<String> getItems() {
        return items;
    }

    @Remove
    public void remove() {
        items = null;
    }
}
