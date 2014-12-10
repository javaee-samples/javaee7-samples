package org.javaee7.jpasamples.schema.gen.scripts.generate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "EMPLOYEE_SCHEMA_GEN_SCRIPTS_GENERATE")
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
