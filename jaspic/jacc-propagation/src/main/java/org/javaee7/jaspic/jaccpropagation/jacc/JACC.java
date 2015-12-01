package org.javaee7.jaspic.jaccpropagation.jacc;

import static java.security.Policy.getPolicy;
import static java.util.logging.Level.SEVERE;

import java.security.CodeSource;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.WebResourcePermission;

/**
 * 
 * @author Arjan Tijms
 * 
 */
public class JACC {
    
    private final static Logger logger = Logger.getLogger(JACC.class.getName());
    
    public static Subject getSubject() {
        try {
            return (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
        } catch (Exception e) {
            logger.log(SEVERE, "", e);
        }
        
        return null;
    }

    public static boolean hasAccess(String uri, Subject subject) {
        return getPolicy().implies(
            new ProtectionDomain(
                new CodeSource(null, (Certificate[]) null), 
                null, null,
                subject.getPrincipals().toArray(new Principal[subject.getPrincipals().size()])
            ),
            new WebResourcePermission(uri, "GET")
        );
    }
}
