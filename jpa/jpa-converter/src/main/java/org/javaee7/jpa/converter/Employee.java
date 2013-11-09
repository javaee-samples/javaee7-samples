package org.javaee7.jpa.converter;

import java.io.Serializable;
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
    
    @Convert(converter = EmployeeDateConverter.class)
    private String dob;
    
    public Employee() { }
    
    public Employee(String name) {
        this.name = name;
    }

    public Employee(int id, String name, String dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    
    @Override
    public String toString() {
        return name + "(" + dob + ")";
    }
}
