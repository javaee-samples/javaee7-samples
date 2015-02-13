package org.javaee7.jaxrs.client.negotiation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.custommonkey.xmlunit.XMLAssert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.xml.sax.SAXException;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
            .addClasses(MyApplication.class, MyResource.class, People.class, Person.class);
    }

    private WebTarget target;

    @ArquillianResource
    private URL base;

    @Before
    public void setUpClass() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/persons").toExternalForm()));
    }

    @Test
    public void testXML() throws SAXException, IOException {
        String xml = target.request("application/xml").get(String.class);
        System.out.println(xml);
        XMLAssert
            .assertXMLEqual(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><people><person><age>1</age><name>Penny</name></person><person><age>2</age><name>Leonard</name></person><person><age>3</age><name>Sheldon</name></person></people>",
                xml);
    }

    @Test
    public void testJSON() throws JSONException {
        String json = target.request("application/json").get(String.class);
        JSONAssert.assertEquals("[{\"age\":1,\"name\":\"Penny\"},{\"age\":2,\"name\":\"Leonard\"},{\"age\":3,\"name\":\"Sheldon\"}]", json, false);
    }

}
