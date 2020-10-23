package com.example.sev_user.captainrocket.Components;


/**
 * Created by sev_user on 8/9/2016.
 */
public abstract class Rocket {
    protected float Xspeed, Yspeed;
    protected float X, Y;
    protected boolean isDrop, isDisappear;

    public Rocket(float xspeed, float x, float y, boolean isDrop, boolean isDisappear) {
        Xspeed = xspeed;
        Yspeed = 0;
        X = x;
        Y = y;
        this.isDrop = isDrop;
        this.isDisappear = isDisappear;
    }

    abstract public void update();

    public float getXspeed() {
        return Xspeed;
    }

    public void setXspeed(float xspeed) {
        Xspeed = xspeed;
    }

    public float getYspeed() {
        return Yspeed;
    }

    public void setYspeed(float yspeed) {
        Yspeed = yspeed;
    }

    public float getX() {
        return X;
    }

    public float getY() {
        return Y;
    }

    public boolean isDrop() {
        return isDrop;
    }


    public void setX(float x) {
        X = x;
    }

    public void setY(float y) {
        Y = y;
    }

    public void setDrop(boolean drop) {
        isDrop = drop;
    }

    public boolean isDisappear() {
        return isDisappear;
    }

    public void setDisappear(boolean disappear) {
        isDisappear = disappear;
    }
}
