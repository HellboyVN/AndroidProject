package com.example.sev_user.captainrocket.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sev_user.captainrocket.R;

/**
 * Created by sev_user on 8/11/2016.
 */
public class Bridge {
    Context context;
    private int X, Y;
    private Bitmap bitmap;


    public Bridge(Context context, int x, int y) {
        this.context = context;
        X = x;
        Y = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bridge);
        bitmap = Bitmap.createScaledBitmap(bitmap, 120, 30, true);
    }

    public void update(float xspeed){
        X -= xspeed;
        if (X <= -10000) X = -10000;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
