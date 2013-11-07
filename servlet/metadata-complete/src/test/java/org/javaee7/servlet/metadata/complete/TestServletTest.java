/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Red Hat and/or its affiliates. All rights reserved.
 */
package org.javaee7.servlet.metadata.complete;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import java.io.File;
import java.io.IOException;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * @author Arun Gupta
 */
//@RunWith(Arquillian.class)
public class TestServletTest {
    
    private static final String WEBAPP_SRC = "src/main/webapp";
    
    /**
     * Arquillian specific method for creating a file which can be deployed
     * while executing the test.
     *
     * @return a war file
     */
//    @Deployment(testable = false)
//    @TargetsContainer("wildfly-arquillian")
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class).
                addClass(TestServlet.class).
                addAsWebInfResource((new File(WEBAPP_SRC + "/WEB-INF", "web.xml")));
        System.out.println(war.toString(true));
        return war;
    }

    /**
     * Test of processRequest method, of class TestServlet.
     */
    @Test
    public void testProcessRequest() throws IOException, SAXException {
        WebConversation conv = new WebConversation();
        GetMethodWebRequest getRequest = new GetMethodWebRequest("http://localhost:8080/metadata-complete/TestServlet");
        WebResponse getResponse = conv.getResponse(getRequest);
        System.out.println(getResponse.getText());
    }
}
