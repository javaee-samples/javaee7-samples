package org.javaee7.jpa.extended.pc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "CHARACTERS")
@NamedQueries({
    @NamedQuery(name = Character.FIND_ALL, query = "SELECT c FROM Character c")
})
public class Character implements Serializable {

    public static final String FIND_ALL = "Character.findAll";

    private static final long serialVersionUID = 1L;

    @Id
    private int id;

    @Column(length = 50)
    private String name;

    public Character() {
    }

    public Character(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Character(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
