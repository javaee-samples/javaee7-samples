/** Copyright Payara Services Limited **/
package org.javaee7.servlet.security.clientcert.jce;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.bouncycastle.jcajce.provider.asymmetric.x509.CertificateFactory;

/**
 * Our own custom CertificateFactory based on the bouncy castle one.
 * 
 * <p>
 * We use this to provide a customized certificate, based on the certificate
 * instance created by bouncy castle.
 * 
 * @author Arjan Tijms
 */
public class MyJCECertificateFactory extends CertificateFactory {

    @Override
    public Certificate engineGenerateCertificate(InputStream in) throws CertificateException {
        Certificate certificate = super.engineGenerateCertificate(in);

        if (certificate instanceof X509Certificate == false) {
            return certificate;
        }

        return new MyJCEX509Certificate((X509Certificate) certificate);
    }

}
