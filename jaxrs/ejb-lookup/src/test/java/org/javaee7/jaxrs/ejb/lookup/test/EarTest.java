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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
 * Tests for use cases with EAR as the deployed archive.
 *
 * @author David Matějček
 */
@RunWith(Arquillian.class)
public class EarTest {

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
     * @return {@link EnterpriseArchive} to deploy to the container.
     * @throws Exception exception
     */
    @Deployment(testable = false)
    public static EnterpriseArchive getArchiveToDeploy() throws Exception {

        final JavaArchive library = ShrinkWrap.create(JavaArchive.class, "default-interface.jar") //
            .addPackages(true, HelloEndpoint.class.getPackage());

        final WebArchive war = ShrinkWrap.create(WebArchive.class, "default-interface.war") //
            .addPackages(true, DefaultInterfaceApplication.class.getPackage()) //
            .addAsLibraries(library) //
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); //
        System.out.println(war.toString(true));

        final WebArchive war1 = createSameBeanNameWar(1,
            org.javaee7.jaxrs.ejb.lookup.sbn.war1.SameBeanNameApplication.class);
        final WebArchive war2 = createSameBeanNameWar(2,
            org.javaee7.jaxrs.ejb.lookup.sbn.war2.SameBeanNameApplication.class);
        final WebArchive war3 = createSameBeanNameWar(3,
            org.javaee7.jaxrs.ejb.lookup.sbn.war3.SameBeanNameApplication.class);

        final EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "jaxrs-ejb-lookup.ear") //
            .addAsModule(war) //
            .addAsModule(war1) //
            .addAsModule(war2) //
            .addAsModule(war3) //
            .setApplicationXML("application.xml");
        System.out.println(ear.toString(true));
        return ear;
    }


    private static WebArchive createSameBeanNameWar(final int id, final Class<?> applicationConfigClass) {
        final WebArchive war = ShrinkWrap.create(WebArchive.class, "same-bean-name-" + id + ".war") //
            .addPackages(true, applicationConfigClass.getPackage()) //
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml"); //
        System.out.println(war.toString(true));
        return war;
    }


    /**
     * Tests stateless bean with no interface.
     *
     * @throws Exception
     */
    @Test
    public void testSimplestBean() throws Exception {
        final WebTarget webTarget = this.targetBase.path("/default-interface/default-interface-app/simplest/greet");
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
        final WebTarget webTarget = this.targetBase
            .path("/default-interface/default-interface-app/iface-in-library/logHello");
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
        final WebTarget webTarget = this.targetBase
            .path("/default-interface/default-interface-app/illegal-interface/ejbMetaData");
        final Response response = webTarget.request(MediaType.TEXT_PLAIN).get();
        assertNotNull("response", response);
        assertEquals("response.status", 204, response.getStatus());
    }


    /**
     * This test is for PAYARA-3922 and PAYARA-3121 and the situation, where ear contains more war
     * files with same bean names and application contexts (three war files in this test).
     * <ol>
     * <li>war1: stateless bean with default name mapping.
     * <li>war2: stateless bean with the name="DuplicitBean2" - should not be in a conflict, because
     * it uses different bean name for mapping and different war context.
     * <li>war3: stateless bean with default name mapping - should not be in a conflict, because
     * it uses different war context.
     * </ol>
     *
     * @throws Exception
     */
    @Test
    public void testSameBeanNames() throws Exception {
        final WebTarget t1 = this.targetBase.path("/same-bean-name-1/same-bean-name-app/same-bean-name-bean/ok");
        final String responseT1 = t1.request(MediaType.APPLICATION_JSON).get(String.class);
        assertEquals("responseT1", "OK1", responseT1);

        final WebTarget t2 = this.targetBase.path("/same-bean-name-2/same-bean-name-app/same-bean-name-bean/ok");
        final String responseT2 = t2.request(MediaType.APPLICATION_JSON).get(String.class);
        assertEquals("responseT2", "OK2", responseT2);

        final WebTarget t3 = this.targetBase.path("/same-bean-name-3/same-bean-name-app/same-bean-name-bean/ok");
        final String responseT3 = t3.request(MediaType.APPLICATION_JSON).get(String.class);
        assertEquals("responseT3", "OK3", responseT3);
    }
}
