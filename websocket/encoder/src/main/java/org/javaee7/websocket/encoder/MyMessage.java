package org.javaee7.websocket.encoder;

import javax.json.JsonObject;

/**
 * @author Arun Gupta
 */
public class MyMessage {
    
    private JsonObject jsonObject;

    public MyMessage() {
    }

    public MyMessage(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }
    
    @Override
    public String toString() {
        return jsonObject.toString();
    }
    
}
