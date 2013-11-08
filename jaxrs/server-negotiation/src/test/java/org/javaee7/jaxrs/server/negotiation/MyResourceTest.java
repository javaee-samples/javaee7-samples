/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jaxrs.server.negotiation;

import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.custommonkey.xmlunit.XMLAssert;
import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.xml.sax.SAXException;

/**
 * @author Arun Gupta
 */
public class MyResourceTest {

    WebTarget target;
    
    @Before
    public void setUp() {
        Client client = ClientBuilder.newClient();
        target = client
                .target("http://localhost:8080/server-negotiation/webresources/persons");
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
        XMLAssert.assertXMLEqual("<collection><person><age>1</age><name>Penny</name></person><person><age>2</age><name>Leonard</name></person><person><age>3</age><name>Sheldon</name></person></collection>", 
                response);
    }
    
}
