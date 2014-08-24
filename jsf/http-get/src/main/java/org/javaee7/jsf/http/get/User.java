package org.javaee7.jsf.http.get;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Named;

/**
 * @author Arun Gupta
 */
@Named
@ApplicationScoped
public class User {

    private String name;

    public String getName() {
        System.out.println("getName: " + name);
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Both process and process2 are valid method signatures
    public void process(ComponentSystemEvent event) throws AbortProcessingException {
        System.out.println("process called");
        name = name.toUpperCase();
    }
    
    public void process2() {
        System.out.println("process2 called");
        name = name.toUpperCase();
    }
}
