/** Portions Copyright Payara Services Limited **/
package org.javaee7;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Various high level Java EE 7 samples specific operations to execute against
 * the various servers used for running the samples
 * 
 * @author arjan
 *
 */
public class ServerOperations {
    
    private static final Logger logger = Logger.getLogger(ServerOperations.class.getName());

    /**
     * Add the default test user and credentials to the identity store of 
     * supported containers
     */
    public static void addUsersToContainerIdentityStore() {
        
        // TODO: abstract adding container managed users to utility class
        // TODO: consider PR for sending CLI commands to Arquillian
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            System.out.println("Adding user for glassfish-remote");
            
            List<String> cmd = new ArrayList<>();
            
            cmd.add("create-file-user");
            cmd.add("--groups");
            cmd.add("g1");
            cmd.add("--passwordfile");
            cmd.add(Paths.get("").toAbsolutePath() + "/src/test/resources/password.txt");
            
            cmd.add("u1");
            
            CliCommands.payaraGlassFish(cmd);
        } else {
            if (javaEEServer == null) {
                System.out.println("javaEEServer not specified");
            } else {
                System.out.println(javaEEServer + " not supported");
            }
        }
        
        // TODO: support other servers than Payara and GlassFish
        
        // WildFly ./bin/add-user.sh -a -u u1 -p p1 -g g1
    }
    
    public static void addCertificateToContainerTrustStore(Certificate clientCertificate) {
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            String gfHome = System.getProperty("glassfishRemote_gfHome");
            if (gfHome == null) {
                logger.info("glassfishRemote_gfHome not specified");
                return;
            }
            
            Path gfHomePath = Paths.get(gfHome);
            if (!gfHomePath.toFile().exists()) {
                logger.severe("glassfishRemote_gfHome at " + gfHome + " does not exists");
                return;
            }
            
            if (!gfHomePath.toFile().isDirectory()) {
                logger.severe("glassfishRemote_gfHome at " + gfHome + " is not a directory");
                return;
            }
            
            // TODO: support current domain
            String domain = System.getProperty("payara_domain", "domain1");
            
            Path cacertsPath = gfHomePath.resolve("glassfish/domains/" + domain + "/config/cacerts.jks");
            
            if (!cacertsPath.toFile().exists()) {
                logger.severe("The trust store at " + cacertsPath.toAbsolutePath() + " does not exists");
                return;
            }
            
            logger.info("*** Adding certificate to: " + cacertsPath.toAbsolutePath());
        
            KeyStore keyStore = null;
            try (InputStream in = new FileInputStream(cacertsPath.toAbsolutePath().toFile())) {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(in, "changeit".toCharArray());
                
                keyStore.setCertificateEntry("arquillianClientTestCert", clientCertificate);
                
                keyStore.store(new FileOutputStream(cacertsPath.toAbsolutePath().toFile()), "changeit".toCharArray());
            } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
                throw new IllegalStateException(e);
            }
        } else {
            if (javaEEServer == null) {
                System.out.println("javaEEServer not specified");
            } else {
                System.out.println(javaEEServer + " not supported");
            }
        }
        
        restartContainer();
    }
    
    public static URL toContainerHttps(URL url) {
        if ("https".equals(url.getProtocol())) {
            return url;
        }
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        // String protocol, String host, int port, String file
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            try {
                URL httpsUrl = new URL(
                    "https",
                    url.getHost(),
                    8181,
                    url.getFile()
                );
                
                System.out.println("Returning " + httpsUrl + " for " + url);
                logger.info("Returning " + httpsUrl + " for " + url);
                
                return httpsUrl;
                
            } catch (MalformedURLException e) {
                System.out.println("Failure creating HTTPS URL");
                e.printStackTrace();
                logger.log(Level.SEVERE, "Failure creating HTTPS URL", e);
            }
            
        } else {
            if (javaEEServer == null) {
                System.out.println("javaEEServer not specified");
            } else {
                System.out.println(javaEEServer + " not supported");
            }
        }
        
        return null;
    }
    
    public static void restartContainer() {
        // Arquillian connectors can stop/start already, but not on demand by code
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            System.out.println("Restarting domain");
            
            List<String> cmd = new ArrayList<>();
            
            cmd.add("restart-domain");
            
            cmd.add(System.getProperty("payara_domain", "domain1"));
            
            CliCommands.payaraGlassFish(cmd);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        } else {
            if (javaEEServer == null) {
                System.out.println("javaEEServer not specified");
            } else {
                System.out.println(javaEEServer + " not supported");
            }
        }
    }
    
    public static void restartContainerDebug() {
        // Arquillian connectors can stop/start already, but not on demand by code
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            System.out.println("Stopping domain");
            
            List<String> cmd = new ArrayList<>();
            
            cmd.add("stop-domain");
            
            CliCommands.payaraGlassFish(cmd);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println("Starting domain");
            
            cmd = new ArrayList<>();
            
            cmd.add("start-domain");
            
            CliCommands.payaraGlassFish(cmd);
            
            System.out.println("Command returned");
            
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.out.println("After sleep");
        }
    }
    
    public static void setupContainerJDBCIDigestIdentityStore() {
        
        String javaEEServer = System.getProperty("javaEEServer");
        
        if ("glassfish-remote".equals(javaEEServer) || "payara-remote".equals(javaEEServer)) {
            
            System.out.println("Setting up container JDBC identity store for " + javaEEServer);
            
            List<String> cmd = new ArrayList<>();
            
            cmd.add("create-auth-realm");
            cmd.add("--classname");
            cmd.add("com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm");
            cmd.add("--property");
            cmd.add(
                "jaas-context=jdbcDigestRealm:" + 
                "encoding=HASHED:" + 
                "password-column=password:" + 
                "datasource-jndi=java\\:comp/DefaultDataSource:" + 
                "group-table=grouptable:"+ 
                "charset=UTF-8:" + 
                "user-table=usertable:" + 
                "group-name-column=groupname:" + 
                "digest-algorithm=None:" + 
                "user-name-column=username");
            
            cmd.add("eesamplesdigestrealm");
            
            CliCommands.payaraGlassFish(cmd);
        } else {
            if (javaEEServer == null) {
                System.out.println("javaEEServer not specified");
            } else {
                System.out.println(javaEEServer + " not supported");
            }
        }
        
        // TODO: support other servers than Payara and GlassFish
        
        // WildFly ./bin/add-user.sh -a -u u1 -p p1 -g g1
    }
}
