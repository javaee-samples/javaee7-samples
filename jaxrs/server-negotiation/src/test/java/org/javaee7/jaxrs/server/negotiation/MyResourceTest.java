package org.javaee7.jaxrs.server.negotiation;

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
import org.skyscreamer.jsonassert.JSONCompareMode;
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

    @ArquillianResource
    private URL base;

    private WebTarget target;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/persons").toExternalForm()));
    }

    @Test
    public void testJson() throws JSONException {
        String response = target.request().accept("application/*").get(String.class);
        JSONAssert.assertEquals("[{\"name\":\"Penny\",\"age\":1},{\"name\":\"Leonard\",\"age\":2},{\"name\":\"Sheldon\",\"age\":3}]",
            response,
            JSONCompareMode.STRICT);
    }

    @Test
    public void testJson2() throws JSONException {
        String response = target.request().get(String.class);
        JSONAssert.assertEquals("[{\"name\":\"Penny\",\"age\":1},{\"name\":\"Leonard\",\"age\":2},{\"name\":\"Sheldon\",\"age\":3}]",
            response,
            JSONCompareMode.STRICT);
    }

    @Test
    public void testXml() throws JSONException, SAXException, IOException {
        String response = target.request().accept("application/xml").get(String.class);
        XMLAssert.assertXMLEqual(
            "<people><person><age>1</age><name>Penny</name></person><person><age>2</age><name>Leonard</name></person><person><age>3</age><name>Sheldon</name></person></people>",
            response);
    }

}
