package org.javaee7.jsf.radio.buttons;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Arun Gupta
 */
public class Movie {
    @Id
    @NotNull
    private Integer id;
    
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    
    @NotNull
    @Size(min = 1, max = 200)
    private String actors;

    public Movie() {
    }

    public Movie(Integer id, String name, String actors) {
        this.id = id;
        this.name = name;
        this.actors = actors;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }    
}
