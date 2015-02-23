package org.javaee7.jpasamples.schema.gen.scripts.external;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "EMPLOYEE_SCHEMA_GEN_SCRIPTS_EXTERNAL")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e")
})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int id;

    @Column(length = 50)
    private String name;

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
