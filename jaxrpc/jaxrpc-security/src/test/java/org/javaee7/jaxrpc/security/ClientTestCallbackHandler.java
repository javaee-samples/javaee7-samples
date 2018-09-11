/** Copyright Payara Services Limited **/
package org.javaee7.jaxrpc.security;

import static javax.xml.rpc.Stub.PASSWORD_PROPERTY;
import static javax.xml.rpc.Stub.USERNAME_PROPERTY;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.sun.xml.wss.impl.callback.EncryptionKeyCallback;
import com.sun.xml.wss.impl.callback.EncryptionKeyCallback.AliasX509CertificateRequest;
import com.sun.xml.wss.impl.callback.PasswordCallback;
import com.sun.xml.wss.impl.callback.UsernameCallback;

/**
 * Callback handler that's used by the generated client stubs to obtain the
 * username and password to insert into the request, and the x.509 certificate
 * to encrypt said username and password.
 * 
 * <p>
 * Note that this only really gets the X.509 certificate from the file src/test/resources/s1as.cert.
 * The username and password already come from the callback and are just being given back to it 
 * (for some reason this is required).
 * 
 * @author Arjan Tijms
 * 
 */
public class ClientTestCallbackHandler implements CallbackHandler {

    private static Logger log = Logger.getLogger(ClientTestCallbackHandler.class.getName());

    public ClientTestCallbackHandler() throws Exception {
        log.info("Instantiating ClientTestCallbackHandler");
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (Callback callback : callbacks) {

            log.info("Processing " + callback);
            
            if (callback instanceof UsernameCallback) {
                UsernameCallback usernameCallback = (UsernameCallback) callback;
                
                usernameCallback.setUsername((String) (usernameCallback.getRuntimeProperties().get(USERNAME_PROPERTY)));
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback passwordCallback = (PasswordCallback) callback;
                
                passwordCallback.setPassword((String) (passwordCallback.getRuntimeProperties().get(PASSWORD_PROPERTY)));
            } else if (callback instanceof EncryptionKeyCallback) {
                EncryptionKeyCallback encryptionKeyCallback = (EncryptionKeyCallback) callback;
                
                AliasX509CertificateRequest request = (AliasX509CertificateRequest) encryptionKeyCallback.getRequest();
                request.setX509Certificate(getCertificate());
            }

        }
    }
    
    private X509Certificate getCertificate() throws FileNotFoundException, IOException {
        try (InputStream inStream = getClass().getClassLoader().getResource("s1as.cert").openStream()) {
            X509Certificate certificate = (X509Certificate) 
                    CertificateFactory.getInstance("X.509")
                                      .generateCertificate(inStream);

            log.info("\nCertificate : " + certificate + "\n");
            
            return certificate;

        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }

}