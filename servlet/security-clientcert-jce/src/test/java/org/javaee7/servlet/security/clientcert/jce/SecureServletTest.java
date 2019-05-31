/** Copyright Payara Services Limited **/
package org.javaee7.servlet.security.clientcert.jce;

import static java.math.BigInteger.ONE;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.logging.Level.FINEST;
import static org.javaee7.ServerOperations.addCertificateToContainerTrustStore;
import static org.javaee7.ServerOperations.addContainerSystemProperty;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.Assert.assertTrue;
import static org.omnifaces.utils.Lang.isEmpty;
import static org.omnifaces.utils.security.Certificates.createTempJKSKeyStore;
import static org.omnifaces.utils.security.Certificates.createTempJKSTrustStore;
import static org.omnifaces.utils.security.Certificates.generateRandomRSAKeys;
import static org.omnifaces.utils.security.Certificates.getCertificateChainFromServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;

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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omnifaces.utils.security.Certificates;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class SecureServletTest {

    private static Logger log = Logger.getLogger(SecureServletTest.class.getName());

    private static final String WEBAPP_SRC = "src/main/webapp";
    
//    static {
//        Security.insertProviderAt(new BouncyCastleProvider(), 1);
//    }

    @ArquillianResource
    private URL base;

    private URL baseHttps;
    private WebClient webClient;
    private static String clientKeyStorePath;

    @Deployment(testable = false)
    public static WebArchive createDeployment() throws FileNotFoundException, IOException {
        
        // Note for JDK 11+, the server needs to be run with a sufficiently new version of JDK 11 or 12.
        // Older versions throw this exception:
        
        //  java.lang.UnsupportedOperationException: Not supported yet.
        //        at java.base/sun.security.ssl.HandshakeHash$CloneableHash.archived(HandshakeHash.java:616)
        //        at java.base/sun.security.ssl.HandshakeHash$T12HandshakeHash.archived(HandshakeHash.java:546)
        //        at java.base/sun.security.ssl.HandshakeHash.archived(HandshakeHash.java:188)
        //        at java.base/sun.security.ssl.CertificateVerify$T12CertificateVerifyMessage.<init>(CertificateVerify.java:650)
        //        at java.base/sun.security.ssl.CertificateVerify$T12CertificateVerifyConsumer.consume(CertificateVerify.java:771)
        //        at java.base/sun.security.ssl.SSLHandshake.consume(SSLHandshake.java:392)
        //        at java.base/sun.security.ssl.HandshakeContext.dispatch(HandshakeContext.java:448)
        //        at java.base/sun.security.ssl.SSLEngineImpl$DelegatedTask$DelegatedAction.run(SSLEngineImpl.java:1065)
        //        at java.base/sun.security.ssl.SSLEngineImpl$DelegatedTask$DelegatedAction.run(SSLEngineImpl.java:1052)
        //        at java.base/java.security.AccessController.doPrivileged(Native Method)
        //        at java.base/sun.security.ssl.SSLEngineImpl$DelegatedTask.run(SSLEngineImpl.java:999)

        // See https://bugs.openjdk.java.net/browse/JDK-8214098
        
        // Works:
        // OpenJDK Runtime Environment Zulu11.31+11-CA (build 11.0.3+7-LTS) 
        
        // Doesn't work:
        // openjdk version "11.0.3" 2019-04-16
        // OpenJDK Runtime Environment (build 11.0.3+7-Ubuntu-1ubuntu1)

        System.out.println("\n*********** DEPLOYMENT START ***************************");
        
        Security.addProvider(new BouncyCastleProvider());

        // Enable to get detailed logging about the SSL handshake on the client
        // For an explanation of the TLS handshake see: https://tls.ulfheim.net
        if (System.getProperty("ssl.debug") != null) {
            enableSSLDebug();
        }
        

        System.out.println("################################################################");

        // ### Generate keys for the client, create a certificate, and add those to a new local key store

        // Generate a Private/Public key pair for the client
        KeyPair clientKeyPair = generateRandomRSAKeys();

        // Create a certificate containing the client public key and signed with the private key
        X509Certificate clientCertificate = createSelfSignedCertificate(clientKeyPair);

        // Create a new local key store containing the client private key and the certificate
        clientKeyStorePath = createTempJKSKeyStore(clientKeyPair.getPrivate(), clientCertificate);
        
        // Only test TLS v1.2 for now
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        
        // Enable to get detailed logging about the SSL handshake on the server
        
        if (System.getProperty("ssl.debug") != null) {
            System.out.println("Setting server SSL debug on");
            addContainerSystemProperty("javax.net.debug", "ssl:handshake");
        }

        // Add the client certificate that we just generated to the trust store of the server.
        // That way the server will trust our certificate.
        // Set the actual domain used with -Dpayara_domain=[domain name] 
        addCertificateToContainerTrustStore(clientCertificate);

        return create(WebArchive.class)
                .addAsLibraries(Maven.resolver()
                        .loadPomFromFile("pom.xml")
                        .resolve("org.bouncycastle:bcprov-jdk15on", "org.bouncycastle:bcpkix-jdk15on")
                        .withTransitivity()
                        .as(JavaArchive.class))
                .addClass(BouncyServlet.class)
                .addClasses(SecureServlet.class)
                .addClasses(MyJCECertificateFactory.class, MyJCEX509Certificate.class)
                .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")))
                .addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "glassfish-web.xml")));
    }

    @Before
    public void setup() throws FileNotFoundException, IOException {
        
        System.out.println("\n*********** SETUP START ***************************");
        
        String algorithms = Security.getProperty("jdk.tls.disabledAlgorithms");
        
        // PSS can't be used with JDK11 and 12, will likely be fixed in JDK13
        // See https://bugs.openjdk.java.net/browse/JDK-8216039
        Security.setProperty("jdk.tls.disabledAlgorithms",  algorithms + " ,RSASSA-PSS");
        
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
        
        if (!isEmpty(serverCertificateChain)) {
            
            System.out.println("Obtained certificate from server. Storing it in client trust store");
        
            String trustStorePath = createTempJKSTrustStore(serverCertificateChain);
    
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
       
        System.out.println("Using client key store from: " + clientKeyStorePath);

        // Client -> Server : the key store's private keys and certificates are used to sign
        // and sent a reply to the server
        webClient.getOptions().setSSLClientCertificate(new File(clientKeyStorePath).toURI().toURL(), "changeit", "jks");
        webClient.getOptions().setTimeout(0);
        
        
        // First do a request to install Bouncy Castle as provider
        // This is a normal HTTP request and doesn't use certificate authentication
        TextPage pageb = webClient.getPage(base + "BouncyServlet");
        log.log(INFO, "Bouncy Castle provider inserted at position: {0}", pageb.getContent());

        System.out.println("*********** SETUP DONE ***************************\n");
    }

    @After
    public void tearDown() throws IOException {
        // Remove Bouncy Castle as provider
        TextPage pageb = webClient.getPage(new WebRequest(new URL(base + "BouncyServlet"), HttpMethod.DELETE));
        log.log(INFO, "Bouncy Castle provider removed: {0}", pageb.getContent());

        webClient.getCookieManager().clearCookies();
        
        // Internally throws:
        //
        //        TransportContext.java:313|Fatal (INTERNAL_ERROR): closing inbound before receiving peer's close_notify (
        //        "throwable" : {
        //          javax.net.ssl.SSLException: closing inbound before receiving peer's close_notify
        //            at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:133)
        //            at java.base/sun.security.ssl.Alert.createSSLException(Alert.java:117)
        //            at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:308)
        //            at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:264)
        //            at java.base/sun.security.ssl.TransportContext.fatal(TransportContext.java:255)
        //            at java.base/sun.security.ssl.SSLSocketImpl.shutdownInput(SSLSocketImpl.java:645)
        //            at java.base/sun.security.ssl.SSLSocketImpl.shutdownInput(SSLSocketImpl.java:624)
        //            at org.apache.http.impl.BHttpConnectionBase.close(BHttpConnectionBase.java:325)
        //            at org.apache.http.impl.conn.LoggingManagedHttpClientConnection.close(LoggingManagedHttpClientConnection.java:81)
        //            at org.apache.http.impl.conn.CPoolEntry.closeConnection(CPoolEntry.java:70)
        //            at org.apache.http.impl.conn.CPoolEntry.close(CPoolEntry.java:96)
        //            at org.apache.http.pool.AbstractConnPool.shutdown(AbstractConnPool.java:148)
        //            at org.apache.http.impl.conn.PoolingHttpClientConnectionManager.shutdown(PoolingHttpClientConnectionManager.java:411)
        //            at com.gargoylesoftware.htmlunit.HttpWebConnection.close(HttpWebConnection.java:1011)
        //
        // Visible when -Dssl.debug is used
        //
        // Should be fixed in JDK11.03, but isn't? 
        // See https://stackoverflow.com/questions/52016415/jdk-11-ssl-error-on-valid-certificate-working-in-previous-versions
        webClient.close();
        System.out.println("\n*********** TEST END ***************************\n");
    }

    @Test
    public void testGetWithCorrectCredentials() throws Exception {

        System.out.println("\n*********** TEST START ***************************\n");
        
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        try {

            // With Bouncy Castle installed, do the request via HTTPS to the secured
            // Servlet
            TextPage page = webClient.getPage(baseHttps + "SecureServlet");

            log.info(page.getContent());
            
            assertTrue("my GET", page.getContent().contains("principal CN=u1"));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    // Private methods

    // TODO: may move these to utility class

    private static X509Certificate createSelfSignedCertificate(KeyPair keys) {
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
   
    
    private static URL getHostFromCertificate(X509Certificate[] serverCertificateChain, URL existingURL) {
        try {
            URL httpsUrl = new URL(
                existingURL.getProtocol(),
                Certificates.getHostFromCertificate(serverCertificateChain),
                existingURL.getPort(),
                existingURL.getFile()
            );
            
            System.out.println("Changing base URL from " + existingURL + " into " + httpsUrl + "\n");
            
            return httpsUrl;
            
        } catch (MalformedURLException e) {
            System.out.println("Failure creating HTTPS URL");
            e.printStackTrace();
            
            System.out.println("FAILED to get CN. Using existing URL: " + existingURL);
            
            return existingURL;
        }
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
