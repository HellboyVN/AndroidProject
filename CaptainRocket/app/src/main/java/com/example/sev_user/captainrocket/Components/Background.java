package com.example.sev_user.captainrocket.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.sev_user.captainrocket.R;

/**
 * Created by sev_user on 8/9/2016.
 */
public class Background {
    private int X;
    Context context;
    private int screenX, screenY;
    private Bitmap bitmap;
    private float XSpeed;

    int scaledXsize;

    public Background(Context context, float XSpeed, int x, int screenX, int screenY, int idBackGround) {
        this.context = context;
        this.XSpeed = XSpeed;
        this.screenX = screenX;
        this.screenY = screenY;
        X = x;

        switch (idBackGround) {
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background1);
                break;
            default:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.background2);
                break;
        }


        int tempX = bitmap.getWidth();
        int tempY = bitmap.getHeight();
        scaledXsize = screenY * tempX / tempY;
        bitmap = Bitmap.createScaledBitmap(bitmap, scaledXsize, screenY, true);

    }

    public void update(float xspeed) {
        this.XSpeed = xspeed;
        X -= xspeed;
        if (X <= -scaledXsize / 2) {
            X += scaledXsize / 2;
        }
    }

    public void destroy() {
        bitmap.recycle();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return X;
    }

    public float getXSpeed() {
        return XSpeed;
    }

    public int getScaledXsize() {
        return scaledXsize;
    }
}
