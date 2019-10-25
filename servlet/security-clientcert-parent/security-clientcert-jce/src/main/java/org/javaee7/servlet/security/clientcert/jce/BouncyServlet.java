/** Copyright Payara Services Limited **/
package org.javaee7.servlet.security.clientcert.jce;

import java.io.IOException;
import java.security.Security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * This Servlet is used to set our custom JCE provider.
 * 
 * @author Arjan Tijms
 */
@WebServlet(urlPatterns = { "/BouncyServlet" })
public class BouncyServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        BouncyCastleProvider provider = new BouncyCastleProvider();
        provider.put("CertificateFactory.X.509", MyJCECertificateFactory.class.getName());
        
        // Installs the JCE provider
        int pos = Security.insertProviderAt(provider, 1);
        
        // Returns the position of the JCE provider, this should be 1.
        response.getWriter().print("pos:" + pos);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
    }

}
