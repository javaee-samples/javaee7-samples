package org.javaee7.websocket.whiteboard;

import java.io.StringReader;
import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * @author Arun Gupta
 */
public class FigureDecoder implements Decoder.Text<Figure> {

    private static final Logger LOGGER = Logger.getLogger(FigureDecoder.class.getName());

    @Override
    public Figure decode(String string) throws DecodeException {
        LOGGER.log(Level.INFO, "decoding: {0}", string);
        JsonObject jsonObject = Json.createReader(new StringReader(string)).readObject();
        return new Figure(jsonObject);
    }

    @Override
    public boolean willDecode(String string) {
        try {
            Json.createReader(new StringReader(string)).readObject();
            return true;
        } catch (JsonException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public void init(EndpointConfig ec) {
    }

    @Override
    public void destroy() {
    }
}
