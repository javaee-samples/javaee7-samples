package org.javaee7.jpa.nativesql.resultset.mapping;

import java.io.Serializable;
import javax.persistence.*;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "EMPLOYEE_NATIVE_SQL_RESULTSET_MAPPING")
@SqlResultSetMapping(name = "myMapping",
                     entities = {@EntityResult(entityClass = Employee.class,
                                               fields = {@FieldResult(name = "identifier", column = "id"),
                                                         @FieldResult(name = "simpleName", column = "name")})})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private int identifier;

    @Column(length = 50)
    private String simpleName;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}
