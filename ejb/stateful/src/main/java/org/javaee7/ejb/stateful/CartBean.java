package org.javaee7.ejb.stateful;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;

/**
 * @author Arun Gupta
 */
@Stateful
public class CartBean {

    List<String> items;

    public CartBean() {
        items = new ArrayList<>();
    }

    public void addItem(String item) {
        items.add(item);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public void purchase() { 
        //. . .
    }
    
    public List<String> getItems() {
        return items;
    }

    @Remove
    public void remove() {
        items = null;
    }
}
