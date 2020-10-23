package hellboy.bka.tlvan.rocketwarrior.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import hellboy.bka.tlvan.rocketwarrior.R;
import hellboy.bka.tlvan.rocketwarrior.StaticVariable.GameSetup;


/**
 * Created by sev_user on 8/11/2016.
 */
public class Bridge {
    Context context;
    private int X, Y;
    private Bitmap bitmap,bitmap_gui_left,bitmap_gui_right;


    public Bridge(Context context, int x, int y) {
        this.context = context;
        X = x;
        Y = y;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bridge);
        bitmap = Bitmap.createScaledBitmap(bitmap, GameSetup.dp2Px(60,context), GameSetup.dp2Px(15,context), true);
//        bitmap_gui_left = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left);
//        bitmap_gui_left =  Bitmap.createScaledBitmap(bitmap_gui_left, 120, 30, true);
//        bitmap_gui_right = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow_left);
//        bitmap_gui_right =  Bitmap.createScaledBitmap(bitmap_gui_right, 120, 30, true);
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
    public Bitmap getBitmapGuiLeft() {
        return bitmap_gui_left;
    }
    public Bitmap getBitmapGuiRight() {
        return bitmap_gui_right;
    }
}
