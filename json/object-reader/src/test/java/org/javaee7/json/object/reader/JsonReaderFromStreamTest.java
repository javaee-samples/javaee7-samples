package org.javaee7.json.object.reader;

import java.io.File;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

/**
 * @author Arun Gupta
 */
@RunWith(Arquillian.class)
public class JsonReaderFromStreamTest {

    @Deployment
    public static Archive<?> deploy() {
        File[] requiredLibraries = Maven.resolver().loadPomFromFile("pom.xml")
            .resolve("org.json:json", "org.skyscreamer:jsonassert")
            .withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
            .addAsResource("1.json")
            .addAsResource("2.json")
            .addAsResource("3.json")
            .addAsResource("4.json")
            .addAsLibraries(requiredLibraries);
    }

    @Test
    public void testEmptyObject() throws JSONException {
        JsonReader jsonReader = Json.createReader(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/1.json"));
        JsonObject json = jsonReader.readObject();

        assertNotNull(json);
        assertTrue(json.isEmpty());
    }

    @Test
    public void testSimpleObjectWithTwoElements() throws JSONException {
        JsonReader jsonReader = Json.createReader(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/2.json"));
        JsonObject json = jsonReader.readObject();

        assertNotNull(json);
        assertFalse(json.isEmpty());
        assertTrue(json.containsKey("apple"));
        assertEquals("red", json.getString("apple"));
        assertTrue(json.containsKey("banana"));
        assertEquals("yellow", json.getString("banana"));
    }

    @Test
    public void testArray() throws JSONException {
        JsonReader jsonReader = Json.createReader(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/3.json"));
        JsonArray jsonArr = jsonReader.readArray();
        assertNotNull(jsonArr);
        assertEquals(2, jsonArr.size());

        JSONAssert.assertEquals("{\"apple\":\"red\"}", jsonArr.get(0).toString(), JSONCompareMode.STRICT);
        JSONAssert.assertEquals("{\"banana\":\"yellow\"}", jsonArr.get(1).toString(), JSONCompareMode.STRICT);
    }

    @Test
    public void testNestedStructure() throws JSONException {
        JsonReader jsonReader = Json.createReader(Thread
            .currentThread()
            .getContextClassLoader()
            .getResourceAsStream("/4.json"));
        JsonObject json = jsonReader.readObject();

        assertNotNull(json);
        assertFalse(json.isEmpty());
        assertTrue(json.containsKey("title"));
        assertEquals("The Matrix", json.getString("title"));
        assertTrue(json.containsKey("year"));
        assertEquals(1999, json.getInt("year"));
        assertTrue(json.containsKey("cast"));
        JsonArray jsonArr = json.getJsonArray("cast");
        assertNotNull(jsonArr);
        assertEquals(3, jsonArr.size());

        JSONAssert.assertEquals("["
            + "    \"Keanu Reaves\","
            + "    \"Laurence Fishburne\","
            + "    \"Carrie-Anne Moss\""
            + "  ]", jsonArr.toString(), JSONCompareMode.STRICT);
    }
}
