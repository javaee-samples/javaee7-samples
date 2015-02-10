package org.javaee7.websocket.whiteboard;

/**
 * @author Arun Gupta
 */
public class Coordinates {
    private float x;
    private float y;

    public Coordinates() {
    }

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
