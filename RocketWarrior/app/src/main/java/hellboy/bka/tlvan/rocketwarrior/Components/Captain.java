package hellboy.bka.tlvan.rocketwarrior.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import hellboy.bka.tlvan.rocketwarrior.R;
import hellboy.bka.tlvan.rocketwarrior.StaticVariable.GameSetup;

/**
 * Created by sev_user on 8/9/2016.
 */
public class Captain {
    // static
    public static final String TAG = "123";
    private float MAX_CAP_YSPEED = GameSetup.MAX_CAP_YSPEED;
    private float GRAVITY = GameSetup.GRAVITY;

    // attributes
    private float X, Y, Xspeed, YSpeed;
    private float screenX, screenY;
    private boolean isJump, isFall, isAlive;
    private Bitmap bitmap, bitmapFront, bitmapBack;
    Context context;

    int type;

    public Captain(Context context, float x, float y, int screenX, int screenY, int type) {
        this.context = context;
        GRAVITY = -GameSetup.dp2Px(1,context);
        MAX_CAP_YSPEED = -GameSetup.dp2Px(20,context);
        X = x;
        Y = y;
        this.Xspeed = 0;
        this.YSpeed = MAX_CAP_YSPEED;
        this.screenX = screenX;
        this.screenY = screenY;
        this.type = type;
        isAlive = true;
        isJump = false;
        isFall = false;
        switch (type) {
            case 1:
                bitmapFront = BitmapFactory.decodeResource(context.getResources(), R.drawable.pikalong_right);
                bitmapFront = Bitmap.createScaledBitmap(bitmapFront, GameSetup.dp2Px(40,context),  GameSetup.dp2Px(25,context), true);
                bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.pikalong_left);
                bitmapBack = Bitmap.createScaledBitmap(bitmapBack,  GameSetup.dp2Px(40,context),  GameSetup.dp2Px(25,context), true);
                bitmap = bitmapFront;
                break;
            case 2:
                bitmapFront = BitmapFactory.decodeResource(context.getResources(), R.drawable.flappy3);
                bitmapFront = Bitmap.createScaledBitmap(bitmapFront, GameSetup.dp2Px(33,context), GameSetup.dp2Px(33,context), true);
                bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.flappy4);
                bitmapBack = Bitmap.createScaledBitmap(bitmapBack, GameSetup.dp2Px(33,context), GameSetup.dp2Px(33,context), true);
                bitmap = bitmapFront;
                break;
            case 3:
                bitmapFront = BitmapFactory.decodeResource(context.getResources(), R.drawable.captain3_front);
                bitmapFront = Bitmap.createScaledBitmap(bitmapFront, GameSetup.dp2Px(33,context), GameSetup.dp2Px(33,context), true);
                bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.captain3_back);
                bitmapBack = Bitmap.createScaledBitmap(bitmapBack, GameSetup.dp2Px(33,context), GameSetup.dp2Px(33,context), true);
                bitmap = bitmapFront;
                break;
            default:
                bitmapFront = BitmapFactory.decodeResource(context.getResources(), R.drawable.captain4_front);
                bitmapFront = Bitmap.createScaledBitmap(bitmapFront, GameSetup.dp2Px(33,context), GameSetup.dp2Px(33,context), true);
                bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.captain4_back);
                bitmapBack = Bitmap.createScaledBitmap(bitmapBack, GameSetup.dp2Px(33,context), GameSetup.dp2Px(33,context), true);
                bitmap = bitmapFront;
                break;
        }
    }

    public void update(float xspeed) {
        // check if captain be at the top of screen
        if (Y >= screenY) {
            isJump = true;
            isFall = true;
            YSpeed = 0;
        }

        // check status & update Y
        if (!isJump && !isFall) YSpeed = MAX_CAP_YSPEED;
            // captain is walking
        else if (isJump & !isFall) {
            // captain is jumping
            if (YSpeed < 0) {
                YSpeed -= GRAVITY;
                if (Y + YSpeed > 0) {
                    Y += YSpeed;
                } else {
                    isFall = true;
                    Y = 0;
                }
            } else {
                isFall = true;
                YSpeed = 0;
            }
        } else if (isJump && isFall) {
            // captain is at peek
//            YSpeed = 0;
            isJump = false;
        } else if (!isJump && isFall) {
            // captain is falling
            if (Y < screenY * 0.8) {
                YSpeed -= GRAVITY;
                Y += YSpeed;
            } else {
                Y = screenY * 0.8f;
                // captain die
                isAlive = false;
//                Jump(xspeed);
            }
        }
        // update X
        if (X >= 0 && X <= screenX * GameSetup.SCREEN_THRESHOLD) {
            float Xtemp = X + xspeed;
            if (Xtemp <= 0) Xtemp = 0;
            if (Xtemp >= screenX * GameSetup.SCREEN_THRESHOLD)
                Xtemp = screenX * GameSetup.SCREEN_THRESHOLD;
            X = Xtemp;
        }
        // update face direction
        if (xspeed >= 0)
            // face forward
            bitmap = bitmapFront;
        else
            // face back
            bitmap = bitmapBack;
    }

    public void Jump(float xspeed) {
        YSpeed = MAX_CAP_YSPEED;
        isJump = true;
        isFall = false;
        update(xspeed);
    }

    public boolean isJumpAboveRocket(Rocket rocket) {
        float tempX = rocket.getX() - X;
        float tempY = rocket.getY() - Y;
        // xu ly va cham
        if (isFall()) {
            // captain is falling
//            if (tempX > -70 && tempX < 20 && tempY > 15 && tempY < 50) {
            if (tempX >= Xspeed + rocket.getXspeed() - GameSetup.dp2Px(40,context) && tempX <= GameSetup.dp2Px(25,context) + Xspeed + rocket.getXspeed() && tempY >= GameSetup.dp2Px(20,context) && tempY <= GameSetup.dp2Px(20,context) + YSpeed - GRAVITY) {
                Y = rocket.getY() - GameSetup.dp2Px(25,context);
                return true;
            }
        }
        return false;
    }

    public boolean isAttacked(Rocket rocket) {
        float tempX = rocket.getX() - X;
        float tempY = rocket.getY() - Y;
        if (tempX <= GameSetup.dp2Px(30,context) + Xspeed && tempX >= GameSetup.dp2Px(25,context) + Xspeed && tempY >= -GameSetup.dp2Px(10,context) && tempY <= GameSetup.dp2Px(15,context)) {
            return true;
        }
        if (tempX >= Xspeed + rocket.getXspeed() - GameSetup.dp2Px(40,context) && tempX <= GameSetup.dp2Px(25,context) + Xspeed + rocket.getXspeed() && tempY <= -GameSetup.dp2Px(15,context) && tempY >= YSpeed - GameSetup.dp2Px(15,context)) {
            return true;
        }
        return false;
    }


    public void destroy() {
        bitmapFront.recycle();
        bitmapBack.recycle();
        bitmap.recycle();
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }

    public float getX() {
        return X;
    }

    public boolean isJump() {
        return isJump;
    }

    public boolean isFall() {
        return isFall;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
