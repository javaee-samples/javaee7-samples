package org.javaee7.json.object.builder;

import java.io.File;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;
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
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class DOMGeneratorTest {

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
        JsonObject jsonObject = Json.createObjectBuilder().build();
        StringWriter w = new StringWriter();
        try (JsonWriter writer = Json.createWriter(w)) {
            writer.write(jsonObject);
        }
        JSONAssert.assertEquals("{}", w.toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testSimpleObject() throws JSONException {
        JsonObject jsonObject = Json.createObjectBuilder()
            .add("apple", "red")
            .add("banana", "yellow")
            .build();
        StringWriter w = new StringWriter();
        try (JsonWriter writer = Json.createWriter(w)) {
            writer.write(jsonObject);
        }
        JSONAssert.assertEquals("{\"apple\" : \"red\", \"banana\" : \"yellow\" }", w.toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testArray() throws JSONException {
        JsonArray jsonArray = Json.createArrayBuilder()
            .add(Json.createObjectBuilder().add("apple", "red"))
            .add(Json.createObjectBuilder().add("banana", "yellow"))
            .build();
        StringWriter w = new StringWriter();
        try (JsonWriter writer = Json.createWriter(w)) {
            writer.write(jsonArray);
        }
        JSONAssert.assertEquals("[{\"apple\":\"red\"},{\"banana\":\"yellow\"}]", w.toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testNestedStructure() throws JSONException {
        JsonObject jsonObject = Json.createObjectBuilder()
            .add("title", "The Matrix")
            .add("year", 1999)
            .add("cast", Json.createArrayBuilder()
                .add("Keanu Reaves")
                .add("Laurence Fishburne")
                .add("Carrie-Anne Moss"))
            .build();
        StringWriter w = new StringWriter();
        try (JsonWriter writer = Json.createWriter(w)) {
            writer.write(jsonObject);
        }
        JSONAssert.assertEquals("{\"title\":\"The Matrix\",\"year\":1999,\"cast\":[\"Keanu Reaves\",\"Laurence Fishburne\",\"Carrie-Anne Moss\"]}", w.toString(),
            JSONCompareMode.STRICT);
    }
}
