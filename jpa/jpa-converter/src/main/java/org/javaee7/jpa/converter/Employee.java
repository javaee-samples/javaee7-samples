package org.javaee7.jpa.converter;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name="EMPLOYEE_SCHEMA_CONVERTER")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int id;
    
    @Column(length=50)
    private String name;
    
    @Convert(converter = CreditCardConverter.class)
    private CreditCard card;
    
    public Employee() { }
    
    public Employee(String name) {
        this.name = name;
    }

    public Employee(int id, String name, CreditCard card) {
        this.id = id;
        this.name = name;
        this.card = card;
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

    public CreditCard getCard() {
        return card;
    }

    public void setCard(CreditCard card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return name + " (" + card + ")";
    }
}
