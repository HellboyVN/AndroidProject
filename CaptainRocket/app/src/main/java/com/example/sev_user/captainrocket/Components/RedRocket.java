package com.example.sev_user.captainrocket.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.sev_user.captainrocket.R;
import com.example.sev_user.captainrocket.StaticVariable.GameSetup;

/**
 * Created by sev_user on 8/8/2016.
 */
public class RedRocket extends Rocket {
    //static variable
    protected static final float GRAVITY = GameSetup.GRAVITY;
    // attributes
    private int type;
    protected Bitmap bitmap;
    protected float screenX, screenY;

    protected int rocketWidth;

    protected Context context;

    public RedRocket(Context context, float xspeed, float y, int type, int screenX, int screenY) {
        super(xspeed, screenX, y, false, false);
        this.context = context;
        this.isDrop = false;
        this.type = type;
        this.isDisappear = false;
        this.screenX = screenX;
        this.screenY = screenY;
        if (type == 1) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_rocket1);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 40, true);

        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_rocket1);
        }
        rocketWidth = bitmap.getWidth();
    }

    public RedRocket(Context context, float xspeed, float y, int screenX, int screenY) {
        super(xspeed, screenX, y, false, false);
        this.context = context;
        this.isDrop = false;
        this.isDisappear = false;
        this.screenX = screenX;
        this.screenY = screenY;
    }


    int flag = 0;

    @Override
    public void update() {
        // update isDisappear
        if ( X<= -rocketWidth || Y >= screenY) isDisappear = true;
        if (isDrop()) {
            flag++;
            this.Yspeed -= GRAVITY;
            Y += Yspeed;
            X -= Xspeed;
        } else {
            X -= Xspeed;
        }
        if (flag == 1){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_rocket1_fall);
            bitmap = Bitmap.createScaledBitmap(bitmap, 120, 40, true);
        }
    }


    public void destroy(){
        bitmap.recycle();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

}
