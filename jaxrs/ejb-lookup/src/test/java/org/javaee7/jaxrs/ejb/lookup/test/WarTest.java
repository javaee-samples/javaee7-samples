// Copyright [2019] [Payara Foundation and/or its affiliates]
package org.javaee7.jaxrs.ejb.lookup.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.javaee7.jaxrs.ejb.lookup.iface.jar.HelloEndpoint;
import org.javaee7.jaxrs.ejb.lookup.iface.war.DefaultInterfaceApplication;
import org.javaee7.jaxrs.ejb.lookup.iface.war.IllegalInterfaceBean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

/**
 * Tests for use cases with WAR as the deployed archive.
 *
 * @author David Matějček
 */
@RunWith(Arquillian.class)
public class WarTest {

    @Rule
    public final TestName name = new TestName();

    @ArquillianResource
    private URL baseUrl;

    private Client client;
    private WebTarget targetBase;


    /**
     * Only to mark the class initialization in logs
     */
    @BeforeClass
    public static void initContainer() {
        System.err.println("initContainer()");
    }


    @Before
    public void before() throws Exception {
        System.err.println("before(). Test name: " + this.name.getMethodName());
        this.client = ClientBuilder.newClient();
        this.targetBase = this.client.target(this.baseUrl.toURI());
    }


    @After
    public void after() {
        System.err.println("after(). Test name: " + this.name.getMethodName());
        this.client.close();
    }


    /**
     * Initializes the deployment unit.
     *
     * @return {@link WebArchive} to deploy to the container.
     * @throws Exception exception
     */
    @Deployment
    public static WebArchive getArchiveToDeploy() throws Exception {
        final JavaArchive library = ShrinkWrap.create(JavaArchive.class, "WarTest-api.jar") //
            .addPackages(true, HelloEndpoint.class.getPackage());

        final WebArchive module = ShrinkWrap.create(WebArchive.class, "WarTest.war") //
            .addPackages(true, DefaultInterfaceApplication.class.getPackage()) //
            .addAsLibraries(library) //
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); //

        System.out.println(module.toString(true));
        return module;
    }


    /**
     * Tests stateless bean with no interface.
     *
     * @throws Exception
     */
    @Test
    public void testSimplestBean() throws Exception {
        final WebTarget webTarget = this.targetBase.path("/default-interface-app/simplest/greet");
        final Response response = webTarget.request(MediaType.TEXT_PLAIN).get();
        assertNotNull("response", response);
        assertEquals("response.status", 200, response.getStatus());
    }


    /**
     * Tests stateless bean with an interface placed in a library (not ejb module), without any
     * Local or Remote interface.
     * This interface still can be used as a local business interface (as if it would be annotated
     * by <code>@Local</code>).
     *
     * @throws Exception
     */
    @Test
    public void testInterfaceWithNoLocalOrRemoteAnnotation() throws Exception {
        final WebTarget webTarget = this.targetBase.path("/default-interface-app/iface-in-library/logHello");
        final Response response = webTarget.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
        assertNotNull("response", response);
        assertEquals("response.status", 204, response.getStatus());
    }


    /**
     * {@link IllegalInterfaceBean} implements an interface from javax.ejb package, which is not
     * allowed. The bean still can be mapped with the usage of it's own name.
     *
     * @throws Exception
     */
    @Test
    public void testBeanWithIllegalInterface() throws Exception {
        final WebTarget webTarget = this.targetBase.path("/default-interface-app/illegal-interface/ejbMetaData");
        final Response response = webTarget.request(MediaType.TEXT_PLAIN).get();
        assertNotNull("response", response);
        assertEquals("response.status", 204, response.getStatus());
    }
}
