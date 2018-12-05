/** Copyright Payara Services Limited **/
package org.javaee7.ejb.remote.ssl;

import static javax.naming.Context.SECURITY_PROTOCOL;
import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static org.omnifaces.utils.security.Certificates.createTempJKSTrustStore;
import static org.omnifaces.utils.security.Certificates.getCertificateChainFromServer;
import static org.omnifaces.utils.security.Certificates.getHostFromCertificate;
import static org.omnifaces.utils.security.Certificates.setSystemTrustStore;

import java.net.URL;
import java.security.cert.X509Certificate;

import javax.naming.Context;
import javax.naming.NamingException;

import org.javaee7.RemoteEJBContextFactory;
import org.javaee7.RemoteEJBContextProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class demonstrates and tests how to request an EJB bean from a remote server.
 *
 * <p>
 * {@link RemoteEJBContextProvider} is used, which is a test artifact abstracting the different
 * ways this is done for different servers.
 *
 * @author Arjan Tijms
 *
 */
@RunWith(Arquillian.class)
public class RemoteBeanTest {
	
	@ArquillianResource
	private URL base;

    private RemoteEJBContextProvider remoteEJBContextProvider;

    @Deployment
    public static Archive<EnterpriseArchive> deployment() {
        try {
        // Add user u1 with password p1 and group g1 to the container's native identity store
        addUsersToContainerIdentityStore();
        
        Archive<EnterpriseArchive> archive =
                // EAR module
                create(EnterpriseArchive.class, "my.ear")
                    .setApplicationXML("META-INF/application.xml")

                    // EJB module
                    .addAsModule(
                        create(JavaArchive.class, "myEJB.jar")
                            .addClasses(Bean.class, BeanRemote.class)
                            .addAsResource("META-INF/glassfish-ejb-jar.xml")
                            .addAsManifestResource(INSTANCE, "beans.xml")
                    )

                    // Web module
                    .addAsModule(
                        create(WebArchive.class, "test.war")
                    );
        
            System.out.println("\n**** Deploying archive: " + archive.toString(true) + " \n");
        
            return archive;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Before
    public void before() {
        remoteEJBContextProvider = RemoteEJBContextFactory.getProvider();
        assumeTrue(
            "No RemoteEJBContextProvider available in current profile",
            remoteEJBContextProvider != null);
    }

    @After
    public void after() {
        remoteEJBContextProvider.releaseContext();
    }

    @Test
    @RunAsClient
    public void callProtectedRemoteBean() throws NamingException {

        // Obtain the JNDI naming context in a vendor specific way.
        Context ejbRemoteContext = remoteEJBContextProvider.getContextWithCredentialsSet("u1", "p1");
        
        ejbRemoteContext.addToEnvironment(SECURITY_PROTOCOL, "ssl");
        
        System.out.println("\n**** Quering server for its certificate at " + base.getHost() + ":" + "3920" + "\n");
        
        // Get the certificate from the server, using the EJB SSL port
        X509Certificate[] serverCertificateChain = getCertificateChainFromServer(base.getHost(), 3920);
        
        for (X509Certificate certificate : serverCertificateChain) {
            System.out.println("\n**** Server presented certificate:" + certificate + " \n");
        }
        
        // Create a trust store on disk containing the servers's certificates
        String trustStorePath = createTempJKSTrustStore(serverCertificateChain);
        
        System.out.println("\n**** Temp trust store with server certificates created at: " + trustStorePath + " \n");
        
        // Set the newly created trust store as the system wide trust store 
        setSystemTrustStore(trustStorePath);
        
        // Get the host name from the certificate the server presented, and use that for the host
        // to ultimately do our SSL request to.
        String host = getHostFromCertificate(serverCertificateChain);
        ejbRemoteContext.addToEnvironment("org.omg.CORBA.ORBInitialHost", host);
        
        System.out.println("\n**** Obtained host \"" + host + "\" from server certificate and will use that for request \n");
        
        // Do the actual request to the server for our remote EJB
        BeanRemote beanRemote = (BeanRemote) ejbRemoteContext.lookup("java:global/my/myEJB/Bean");
        
        System.out.println("\n**** Remote EJB obtained via SSL: " + beanRemote + " \n");

        assertEquals("method", beanRemote.method());
    }

}