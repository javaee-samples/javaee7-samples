package org.javaee7.jaxws.endpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fermin Gallego
 *
 */
public class EBook {
    private String title;
    private int numPages;
    private double price;
    private List<String> notes;

    public EBook() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getNotes() {
        if (notes == null) {
            notes = new ArrayList<String>();
        }
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

}
