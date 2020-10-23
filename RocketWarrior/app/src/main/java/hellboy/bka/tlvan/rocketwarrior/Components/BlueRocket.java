package hellboy.bka.tlvan.rocketwarrior.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import hellboy.bka.tlvan.rocketwarrior.R;
import hellboy.bka.tlvan.rocketwarrior.StaticVariable.GameSetup;


/**
 * Created by sev_user on 8/8/2016.
 */
public class BlueRocket extends RedRocket{

    public BlueRocket(Context context, float v, float ystart, int screenX, int screenY) {
        super(context, v, ystart, screenX, screenY);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_rocket);
        bitmap = Bitmap.createScaledBitmap(bitmap, GameSetup.dp2Px(60,context), GameSetup.dp2Px(20,context), true);
        rocketWidth = bitmap.getWidth();
    }
    float delta = 0;
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
            Y -= 8 * Math.cos(delta);
            delta += 0.1;
            X -= Xspeed;
        }
        if (flag == 1){
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_rocket_fall);
            bitmap = Bitmap.createScaledBitmap(bitmap, GameSetup.dp2Px(60,context), GameSetup.dp2Px(20,context), true);
        }
    }
}
