package org.javaee7.websocket.whiteboard;

import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * @author Arun Gupta
 */
public class Figure {
    private JsonObject json;

    public Figure() {
    }

    public Figure(JsonObject json) {
        this.json = json;
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }

    @Override
    public String toString() {
        StringWriter writer = new StringWriter();
        Json.createWriter(writer).write(json);
        return writer.toString();
    }
}
