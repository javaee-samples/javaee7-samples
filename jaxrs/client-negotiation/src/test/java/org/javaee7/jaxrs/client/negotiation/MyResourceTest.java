/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.javaee7.jaxrs.client.negotiation;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Arun Gupta
 */
public class MyResourceTest {

    private static WebTarget target;
    
    @BeforeClass
    public static void setUpClass() {
        Client client = ClientBuilder.newClient();
        target = client
                .target("http://localhost:8080/client-negotiation/webresources/persons");
    }

    @Test
    public void testXML() {
        String xml = target.request("application/xml").get(String.class);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><people><person><age>1</age><name>Penny</name></person><person><age>2</age><name>Leonard</name></person><person><age>3</age><name>Sheldon</name></person></people>", xml);
    }

    @Test
    public void testJSON() {
        String json = target.request("application/json").get(String.class);
        assertEquals("[{\"age\":1,\"name\":\"Penny\"},{\"age\":2,\"name\":\"Leonard\"},{\"age\":3,\"name\":\"Sheldon\"}]", json);
    }
    
}
