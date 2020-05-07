package org.javaee7.servlet.protocolhandler;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arjan Tijms
 */
@RunWith(Arquillian.class)
public class ProtocolHandlerTest {

    @ArquillianResource
    private URL base;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return 
            ShrinkWrap.create(WebArchive.class)
                     .addClasses(
                         UpgradeServlet.class,
                         MyProtocolHandler.class);
    }

    @Test
    @RunAsClient
    public void testUpgradeProtocol() throws IOException, URISyntaxException {
        
        // Read more manually from the connection, as using the regular readers (JAX-RS client, HtmlUnit)
        // typically hang when reading.
        
        URLConnection connection = new URL(base, "UpgradeServlet").openConnection();
        connection.setRequestProperty("Connection", "Upgrade");
        connection.setRequestProperty("Upgrade", "echo");
        connection.setConnectTimeout(2000);
        connection.setReadTimeout(2000);
        
        StringBuilder response = new StringBuilder();
        
        try (InputStream in = connection.getInputStream()) {
            InputStreamReader reader = new InputStreamReader(in);
            
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 10000) {  // for at most 10 seconds   
                try {
                    char[] buffer = new char[1];
                    reader.read(buffer);
                    
                    System.out.println("Character read = " + buffer[0]);
                    
                    // Use the end of line character is this sample to signal end of transmission
                    if (buffer[0] == '\n') {
                        break;
                    }
                    response.append(buffer[0]);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        assertEquals("In protocol handler", response.toString());
    }
    
}
