/** Copyright Payara Services Limited **/
package org.javaee7.ejb.remote;

import static org.javaee7.ServerOperations.addUsersToContainerIdentityStore;
import static org.jboss.shrinkwrap.api.asset.EmptyAsset.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import javax.naming.Context;
import javax.naming.NamingException;

import org.javaee7.RemoteEJBContextFactory;
import org.javaee7.RemoteEJBContextProvider;
import org.javaee7.ejb.remote.remote.Bean;
import org.javaee7.ejb.remote.remote.BeanRemote;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

    private RemoteEJBContextProvider remoteEJBContextProvider;

    @Deployment
    public static Archive<?> deployment() {

        // Add user u1 with password p1 and group g1 to the container's native identity store
        addUsersToContainerIdentityStore();

        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Bean.class, BeanRemote.class)
                .addAsManifestResource(INSTANCE, "beans.xml");
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

        BeanRemote beanRemote = (BeanRemote) ejbRemoteContext.lookup("java:global/test/Bean");

        assertEquals("method", beanRemote.method());
    }

}