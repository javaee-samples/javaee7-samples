/** Copyright Payara Services Limited **/
package org.javaee7;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;

import com.sun.enterprise.security.auth.login.common.PasswordCredential;
import com.sun.enterprise.security.common.ClientSecurityContext;

/**
 * This class returns a JNDI context suitable for remote lookups against the default URL
 * for a remote Payara or GlassFish server (localhost). It sets the provided credentials
 * in a Payara/GlassFish specific way.
 *
 * @author Arjan Tijms
 *
 */
public class PayaraEJBContextProvider implements RemoteEJBContextProvider {

    @Override
    public Context getContextWithCredentialsSet(String username, String password) {

        // Create a new subject with a password credential
        Subject subject = new Subject();
        subject.getPrivateCredentials().add(new PasswordCredential(username, password.toCharArray(), "default"));

        // Store this subject into a global variable where the CORBA/IIOP code will pick it up.
        ClientSecurityContext.setCurrent(new ClientSecurityContext(username, subject));

        // Note: no need for setting "java.naming.factory.initial", since this is already defined
        // by jndi.properties in the glassfish-naming.jar on the classpath.
        try {
            return new InitialContext();
        } catch (NamingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void releaseContext() {
        ClientSecurityContext.setCurrent(null);
    }

}
