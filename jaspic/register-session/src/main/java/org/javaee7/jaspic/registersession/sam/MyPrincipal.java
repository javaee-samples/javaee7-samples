package org.javaee7.jaspic.registersession.sam;

import java.security.Principal;

/**
 * 
 * @author Arjan Tijms
 * 
 */
public class MyPrincipal implements Principal {

    private final String name;
    
    public MyPrincipal(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
