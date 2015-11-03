package org.javaee7.servlet.filters

import org.jboss.arquillian.container.test.api.Deployment
import org.jboss.arquillian.spock.ArquillianSputnik
import org.jboss.arquillian.test.api.ArquillianResource
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.WebArchive
import org.junit.runner.RunWith
import spock.lang.Specification

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.Response

@RunWith(ArquillianSputnik)
class FilterServletSpecification extends Specification{

    @Deployment(testable = false)
    def static WebArchive "create deployment"() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(CharResponseWrapper.class)
                .addClasses(TestServlet.class, FooBarFilter.class);
    }

    @ArquillianResource
    private URL base;

    def "standard servlet should return a simple text"() {
        given:
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI.create(new URL(base, "TestServlet").toExternalForm()));

        when:
        Response response = target.request().get();

        then:
        response.readEntity(String.class) == "bar"
    }

    def "filtered servlet should return a enhanced foobar text"() {
        given:
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(URI.create(new URL(base, "filtered/TestServlet").toExternalForm()));

        when:
        Response response = target.request().get();

        then:
        response.readEntity(String.class) == "foo--bar--bar"
    }

}
