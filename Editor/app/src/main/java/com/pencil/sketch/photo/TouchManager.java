package com.pencil.sketch.photo;

import android.view.MotionEvent;

public class TouchManager {
    private final int maxNumberOfTouchPoints;
    private final Vector2D[] points;
    private final Vector2D[] previousPoints;

    public TouchManager(int maxNumberOfTouchPoints) {
        this.maxNumberOfTouchPoints = maxNumberOfTouchPoints;
        this.points = new Vector2D[maxNumberOfTouchPoints];
        this.previousPoints = new Vector2D[maxNumberOfTouchPoints];
    }

    public boolean isPressed(int index) {
        return this.points[index] != null;
    }

    public int getPressCount() {
        int count = 0;
        for (Vector2D point : this.points) {
            if (point != null) {
                count++;
            }
        }
        return count;
    }

    public Vector2D moveDelta(int index) {
        if (!isPressed(index)) {
            return new Vector2D();
        }
        return Vector2D.subtract(this.points[index], this.previousPoints[index] != null ? this.previousPoints[index] : this.points[index]);
    }

    private static Vector2D getVector(Vector2D a, Vector2D b) {
        if (a != null && b != null) {
            return Vector2D.subtract(b, a);
        }
        throw new RuntimeException("can't do this on nulls");
    }

    public Vector2D getPoint(int index) {
        return this.points[index] != null ? this.points[index] : new Vector2D();
    }

    public Vector2D getPreviousPoint(int index) {
        return this.previousPoints[index] != null ? this.previousPoints[index] : new Vector2D();
    }

    public Vector2D getVector(int indexA, int indexB) {
        return getVector(this.points[indexA], this.points[indexB]);
    }

    public Vector2D getPreviousVector(int indexA, int indexB) {
        if (this.previousPoints[indexA] == null || this.previousPoints[indexB] == null) {
            return getVector(this.points[indexA], this.points[indexB]);
        }
        return getVector(this.previousPoints[indexA], this.previousPoints[indexB]);
    }

    public void update(MotionEvent event) {
        int actionCode = event.getAction() & 255;
        int index = 0;
        Vector2D[] vector2DArr = null;
        if (actionCode == 6 || actionCode == 1) {
            index = event.getAction() >> 8;
            vector2DArr = this.previousPoints;
            this.points[index] = null;
            vector2DArr[index] = null;
            return;
        }
        for (int i = 0; i < this.maxNumberOfTouchPoints; i++) {
            if (i < event.getPointerCount()) {
                index = event.getPointerId(i);
                Vector2D newPoint = new Vector2D(event.getX(i), event.getY(i));
                if (this.points[index] == null) {
                    this.points[index] = newPoint;
                } else {
                    if (this.previousPoints[index] != null) {
                        this.previousPoints[index].set(this.points[index]);
                    } else {
                        this.previousPoints[index] = new Vector2D(newPoint);
                    }
                    if (Vector2D.subtract(this.points[index], newPoint).getLength() < 64.0f) {
                        this.points[index].set(newPoint);
                    }
                }
            } else {
                vector2DArr = this.previousPoints;
                this.points[i] = null;
                vector2DArr[i] = null;
            }
        }
    }
}
