package org.javaee7.jms.xa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_USERS")
public class User {

    @Id
    private String email;

    public User()
    {
    }

    public User(String email)
    {
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
