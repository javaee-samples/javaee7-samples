package org.javaee7.jsf.radio.buttons;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@SessionScoped
public class MovieBean implements Serializable {

    private static final List<Movie> list = Arrays.asList(
            new Movie(1, "The Matrix", "Keanu Reeves"),
            new Movie(2, "The Lord of the Rings", "Elijah Wood"),
            new Movie(3, "The Inception", "Leonardo Dicaprio"));
    int selected;

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public List<Movie> getAll() {
        return list;
    }

    public Movie getSelectedMovie() {
        return list.get(selected-1);
    }
}
