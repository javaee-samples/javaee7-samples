package org.javaee7.json.streaming.generate;

import java.io.File;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 *
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class StreamingGeneratorTest {

    @Deployment
    public static Archive<?> deploy() {
        File[] requiredLibraries = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("org.json:json", "org.skyscreamer:jsonassert")
            .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addAsLibraries(requiredLibraries);
    }

    @Test
    public void testEmptyObject() throws JSONException {
        JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
        StringWriter w = new StringWriter();
        JsonGenerator gen = factory.createGenerator(w);
        gen.writeStartObject().writeEnd();
        gen.flush();

        JSONAssert.assertEquals("{}", w.toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testSimpleObject() throws JSONException {
        JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
        StringWriter w = new StringWriter();
        JsonGenerator gen = factory.createGenerator(w);

        gen
            .writeStartObject()
            .write("apple", "red")
            .write("banana", "yellow")
            .writeEnd();
        gen.flush();
        JSONAssert.assertEquals("{\"apple\" : \"red\", \"banana\" : \"yellow\" }", w.toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testArray() throws JSONException {
        JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
        StringWriter w = new StringWriter();
        JsonGenerator gen = factory.createGenerator(w);

        gen
            .writeStartArray()
            .writeStartObject()
            .write("apple", "red")
            .writeEnd()
            .writeStartObject()
            .write("banana", "yellow")
            .writeEnd()
            .writeEnd();
        gen.flush();
        JSONAssert.assertEquals("[{\"apple\":\"red\"},{\"banana\":\"yellow\"}]", w.toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testNestedStructure() throws JSONException {
        JsonGeneratorFactory factory = Json.createGeneratorFactory(null);
        StringWriter w = new StringWriter();
        JsonGenerator gen = factory.createGenerator(w);

        gen
            .writeStartObject()
            .write("title", "The Matrix")
            .write("year", 1999)
            .writeStartArray("cast")
            .write("Keanu Reaves")
            .write("Laurence Fishburne")
            .write("Carrie-Anne Moss")
            .writeEnd()
            .writeEnd();
        gen.flush();
        JSONAssert.assertEquals("{\"title\":\"The Matrix\",\"year\":1999,\"cast\":[\"Keanu Reaves\",\"Laurence Fishburne\",\"Carrie-Anne Moss\"]}", w.toString(),
            JSONCompareMode.STRICT);
    }
}
