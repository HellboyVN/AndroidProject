package hellboy.bka.tlvan.rocketwarrior.StaticVariable;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by sev_user on 8/9/2016.
 */
public class GameSetup {

    public static final float GRAVITY = -2;
    public static final float MAX_CAP_YSPEED = -40;
    public static final int MAX_CAP_XSPEED = 6;

    public static final int MIN_RED1_XSPEED = 5;
    public static final int MAX_RED1_XSPEED = 12;


    public static final int MIN_BLUE_XSPEED = 5;
    public static final int MAX_BLUE_XSPEED = 12;

    public static final int MIN_BACKGROUND_XSPEED = 0/2;
    public static final int MAX_BACKGROUND_XSPEED = 8;

    public static float MAX_ROCKET_Y_RATIO = 0.7f;//old value 0.8f
    public static float MIN_ROCKET_Y_RATIO = 0.3f;


    public static float MIN_ROCKET_DISTANCE = 0.2f;
    public static float MAX_ROCKET_DISTANCE = 0.4f;

    public static float SCREEN_THRESHOLD = 0.4f;

    public static int dp2Px(int dp,Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px =(int) (dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
    public static int Px2Dp(int px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int)px / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
