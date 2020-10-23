package com.pencil.sketch.photo;

public class Vector2D {
    private float x;
    private float y;

    public Vector2D(){

    }
    public Vector2D(Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getLength() {
        return (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y)));
    }

    public Vector2D set(Vector2D other) {
        this.x = other.getX();
        this.y = other.getY();
        return this;
    }

    public Vector2D set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2D add(Vector2D value) {
        this.x += value.getX();
        this.y += value.getY();
        return this;
    }

    public static Vector2D subtract(Vector2D lhs, Vector2D rhs) {
        return new Vector2D(lhs.x - rhs.x, lhs.y - rhs.y);
    }

    public static float getDistance(Vector2D lhs, Vector2D rhs) {
        return subtract(lhs, rhs).getLength();
    }

    public static float getSignedAngleBetween(Vector2D a, Vector2D b) {
        Vector2D na = getNormalized(a);
        Vector2D nb = getNormalized(b);
        return (float) (Math.atan2((double) nb.y, (double) nb.x) - Math.atan2((double) na.y, (double) na.x));
    }

    public static Vector2D getNormalized(Vector2D v) {
        float l = v.getLength();
        if (l == 0.0f) {
            return new Vector2D();
        }
        return new Vector2D(v.x / l, v.y / l);
    }

    public String toString() {
        return String.format("(%.4f, %.4f)", new Object[]{Float.valueOf(this.x), Float.valueOf(this.y)});
    }
}
