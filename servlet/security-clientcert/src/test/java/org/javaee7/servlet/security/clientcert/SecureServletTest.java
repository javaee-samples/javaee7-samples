/** Copyright Payara Services Limited **/
package org.javaee7.servlet.security.clientcert;

import static java.math.BigInteger.ONE;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.logging.Level.FINEST;
import static org.javaee7.ServerOperations.addCertificateToContainerTrustStore;
import static org.javaee7.ServerOperations.addContainerSystemProperty;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.javaee7.ServerOperations;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class SecureServletTest {

    private static Logger log = Logger.getLogger(SecureServletTest.class.getName());

    private static final String WEBAPP_SRC = "src/main/webapp";

    @ArquillianResource
    private URL base;

    private URL baseHttps;

    WebClient webClient;

    @Deployment(testable = false)
    public static WebArchive createDeployment() throws FileNotFoundException, IOException {

        System.out.println("\n*********** DEPLOYMENT START ***************************");
        
        Provider provider = new BouncyCastleProvider();
        Security.addProvider(provider);

        // Enable to get detailed logging about the SSL handshake on the client
        
        // For an explanation of the TLS handshake see: https://tls.ulfheim.net
        
        if (System.getProperty("ssl.debug") != null) {
            enableSSLDebug();
        }
        

        System.out.println("################################################################");

        // ### Generate keys for the client, create a certificate, and add those to a new local key store

        // Generate a Private/Public key pair for the client
        KeyPair clientKeyPair = generateRandomKeys();

        // Create a certificate containing the client public key and signed with the private key
        X509Certificate clientCertificate = createSelfSignedCertificate(clientKeyPair);

        // Create a new local key store containing the client private key and the certificate
        createKeyStore(clientKeyPair.getPrivate(), clientCertificate);
        
        // Enable to get detailed logging about the SSL handshake on the server
        
        if (System.getProperty("ssl.debug") != null) {
            System.out.println("Setting server SSL debug on");
            addContainerSystemProperty("javax.net.debug", "ssl:handshake");
        }

        // Add the client certificate that we just generated to the trust store of the server.
        // That way the server will trust our certificate.
        addCertificateToContainerTrustStore(clientCertificate);

        return create(WebArchive.class)
                .addClasses(SecureServlet.class)
                .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
                .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "glassfish-web.xml")));
    }

    @Before
    public void setup() throws FileNotFoundException, IOException {
        
        System.out.println("\n*********** SETUP START ***************************");
        
        webClient = new WebClient();

        // First get the HTTPS URL for which the server is listening
        baseHttps = ServerOperations.toContainerHttps(base);
        if (baseHttps == null) {
            throw new IllegalStateException("No https URL could be created from " + base);
        }

        
        
        // ### Ask the server for its certificate and add that to a new local trust store
        
        // Server -> client : the trust store certificates are used to validate the certificate sent
        // by the server
        X509Certificate[] serverCertificateChain = getCertificateChainFromServer(baseHttps.getHost(), baseHttps.getPort());
        
        if (serverCertificateChain != null && serverCertificateChain.length > 0) {
            
            System.out.println("Obtained certificate from server. Storing it in client trust store");
        
            createTrustStore(serverCertificateChain);
    
            String trustStorePath = System.getProperty("buildDirectory", "") +  "/clientTrustStore.jks";
            System.out.println("Reading trust store from: " + trustStorePath);
            
            webClient.getOptions().setSSLTrustStore(new File(trustStorePath).toURI().toURL(), "changeit", "jks");
            
            // If the use.cnHost property is we try to extract the host from the server
            // certificate and use exactly that host for our requests.
            // This is needed if a server is listening to multiple host names, for instance
            // localhost and example.com. If the certificate is for example.com, we can't
            // localhost for the request, as that will not be accepted.
            if (System.getProperty("use.cnHost") != null) {
                System.out.println("use.cnHost set. Trying to grab CN from certificate and use as host for requests.");
                baseHttps = getHostFromCertificate(serverCertificateChain, baseHttps);
            }
        } else {
            System.out.println("Could not obtain certificates from server. Continuing without custom truststore");
        }
       
        String keyStorePath = System.getProperty("buildDirectory", "") +  "/clientKeyStore.jks";
        System.out.println("Reading key store from: " + keyStorePath);

        // Client -> Server : the key store private keys and certificates are used to sign
        // and sent a reply to the server
        webClient.getOptions().setSSLClientCertificate(new File(keyStorePath).toURI().toURL(), "changeit", "jks");
        
        System.out.println("*********** SETUP DONE ***************************\n");
    }

    @After
    public void tearDown() {
        webClient.getCookieManager().clearCookies();
        webClient.close();
        System.out.println("\n*********** TEST END ***************************\n");
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {
        
        System.out.println("\n*********** TEST START ***************************\n");
        
        try {
            TextPage page = webClient.getPage(baseHttps + "SecureServlet");

            log.info(page.getContent());

            assertTrue("my GET", page.getContent().contains("principal C=UK, ST=lak, L=zak, O=kaz, OU=bar, CN=lfoo"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    // Private methods

    // TODO: may move these to utility class

    private static X509Certificate[] getCertificateChainFromServer(String host, int port) {

        final List<X509Certificate[]> X509Certificates = new ArrayList<>();

        try {
            SSLContext context = SSLContext.getInstance("TLS");

            TrustManager interceptingTrustManager = new X509TrustManager() {

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[] {};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    System.out.println("**** intercepting checkServerTrusted chain" + chain + " authType " + authType);
                    X509Certificates.add(chain);
                }
            };

            context.init(null, new TrustManager[] { interceptingTrustManager }, null);

            SSLSocketFactory factory = context.getSocketFactory();

            try (SSLSocket socket = (SSLSocket) factory.createSocket(host, port)) {
                socket.setSoTimeout(15000);
                socket.startHandshake();
                socket.close();
            }

        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }

        if (!X509Certificates.isEmpty()) {
            X509Certificate[] x509Certificates = X509Certificates.get(0);

            for (X509Certificate certificate : x509Certificates) {
                System.out.println("\n**** Server presented certificate:" + certificate + " \n");
            }

            return x509Certificates;
        }

        return null;
    }

    public static X509Certificate createSelfSignedCertificate(KeyPair keys) {
        try {
            Provider provider = new BouncyCastleProvider();
            Security.addProvider(provider);
            return new JcaX509CertificateConverter()
                    .setProvider(provider)
                    .getCertificate(
                            new X509v3CertificateBuilder(
                                    new X500Name("CN=lfoo, OU=bar, O=kaz, L=zak, ST=lak, C=UK"),
                                    ONE,
                                    Date.from(now()),
                                    Date.from(now().plus(1, DAYS)),
                                    new X500Name("CN=lfoo, OU=bar, O=kaz, L=zak, ST=lak, C=UK"),
                                    SubjectPublicKeyInfo.getInstance(keys.getPublic().getEncoded()))
                    .build(
                            new JcaContentSignerBuilder("SHA256WithRSA")
                                .setProvider(provider)
                                .build(keys.getPrivate())));
        } catch (CertificateException | OperatorCreationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static KeyPair generateRandomKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGenerator.initialize(2048);

            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void createKeyStore(PrivateKey privateKey, X509Certificate certificate) {
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, null);

            keyStore.setEntry(
                    "clientKey",
                    new PrivateKeyEntry(privateKey, new Certificate[] { certificate }),
                    new PasswordProtection("changeit".toCharArray()));

            String path = System.getProperty("buildDirectory", "") +  "/clientKeyStore.jks";

            System.out.println("Storing client key store at: " + path);

            keyStore.store(new FileOutputStream(path), "changeit".toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void createTrustStore(X509Certificate[] certificates) {
        try {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                keyStore.setCertificateEntry("serverCert" + i, certificates[i]);
            }

            String path = System.getProperty("buildDirectory", "") +  "/clientTrustStore.jks";

            System.out.println("Storing trust store at: " + path);

            keyStore.store(new FileOutputStream(path), "changeit".toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static URL getHostFromCertificate(X509Certificate[] serverCertificateChain, URL existingURL) {
        X509Certificate firstCert = serverCertificateChain[0];
        String name = firstCert.getIssuerX500Principal().getName();
        System.out.println("Full certificate issuer name " + name);
        
        String[] names = name.split(",");
        
        // cn should be first
        if (names != null && names.length > 0) {
            String cnNameString = names[0];
            String cn = cnNameString.substring(cnNameString.indexOf('=') + 1).trim();
            System.out.println("Issuer CN name: \"" + cn + "\"");
            
            try {
                URL httpsUrl = new URL(
                    existingURL.getProtocol(),
                    cn,
                    existingURL.getPort(),
                    existingURL.getFile()
                );
                
                System.out.println("Changing base URL from " + existingURL + " into " + httpsUrl + "\n");
                
                return httpsUrl;
                
            } catch (MalformedURLException e) {
                System.out.println("Failure creating HTTPS URL");
                e.printStackTrace();
            }
            
        }
        
        System.out.println("FAILED to get CN. Using existing URL: " + existingURL);
        
        return existingURL;
    }
    
    private static void enableSSLDebug() {
        System.setProperty("javax.net.debug", "ssl:handshake");
        
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "debug");
        Logger.getLogger("com.gargoylesoftware.htmlunit.httpclient.HtmlUnitSSLConnectionSocketFactory").setLevel(FINEST);
        Logger.getLogger("org.apache.http.conn.ssl.SSLConnectionSocketFactory").setLevel(FINEST);
        Log logger = LogFactory.getLog(org.apache.http.conn.ssl.SSLConnectionSocketFactory.class);
        ((Jdk14Logger) logger).getLogger().setLevel(FINEST);
        logger = LogFactory.getLog(com.gargoylesoftware.htmlunit.httpclient.HtmlUnitSSLConnectionSocketFactory.class);
        ((Jdk14Logger) logger).getLogger().setLevel(FINEST);
        Logger.getGlobal().getParent().getHandlers()[0].setLevel(FINEST);
    }

}
