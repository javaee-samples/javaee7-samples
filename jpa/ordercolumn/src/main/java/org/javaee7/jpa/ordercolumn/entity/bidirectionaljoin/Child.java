package org.javaee7.jpa.ordercolumn.entity.bidirectionaljoin;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="Child1")
public class Child {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    @SuppressWarnings("unused")
    private int dummy = 1;

    @ManyToOne
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Parent parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

}
