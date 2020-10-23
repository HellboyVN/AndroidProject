package lp.me.lockos10.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Vibrator;
import lp.me.lockos10.R;

public class Utils {
    public static final int PICK_IMAGE = 12457;

    public static Typeface getTypefaceRobotoThin(Context Context) {
        try {
            Typeface tf = Typeface.createFromAsset(Context.getAssets(), "fonts/Roboto-Thin.ttf");
            if (tf != null) {
                return tf;
            }
            return Typeface.DEFAULT;
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static Typeface getTypefaceRobotoRegular(Context Context) {
        try {
            Typeface tf = Typeface.createFromAsset(Context.getAssets(), "fonts/Roboto-Regular.ttf");
            if (tf != null) {
                return tf;
            }
            return Typeface.DEFAULT;
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static Typeface getTypefaceRobotoMedium(Context Context) {
        try {
            Typeface tf = Typeface.createFromAsset(Context.getAssets(), "fonts/Roboto-Medium.ttf");
            if (tf != null) {
                return tf;
            }
            return Typeface.DEFAULT;
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static Typeface getTypefaceRobotoLight(Context Context) {
        try {
            Typeface tf = Typeface.createFromAsset(Context.getAssets(), "fonts/Roboto-Light.ttf");
            if (tf != null) {
                return tf;
            }
            return Typeface.DEFAULT;
        } catch (Exception e) {
            return Typeface.DEFAULT;
        }
    }

    public static void vibrate(Context context) {
        ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(300);
    }

    public static void shareClick(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + text);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void gotoMarket(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void gotoMarket(Context context, String url) {
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(url)));
    }

    public static String getDateInWeek(Context context, int i) {
        return context.getResources().getStringArray(R.array.dayinweek)[i - 1];
    }

    public static String getMonthString(Context context, int i) {
        return context.getResources().getStringArray(R.array.month)[i - 1];
    }

    public static boolean wifiConected(Context context) {
        if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getNetworkInfo(1).isConnected()) {
            return true;
        }
        return false;
    }

    public static void getBackgroundEx(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public static void sound(Activity context, final int idSound) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        context.setVolumeControlStream(3);
        SoundPool soundPool = new SoundPool(20, 3, 0);
        final int soundID = soundPool.load(context, idSound, 1);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (idSound == R.raw.type) {
                    soundPool.play(soundID, 0.1f, 0.1f, 1, 0, 1.0f);
                    return;
                }
                soundPool.play(soundID, 0.5f, 0.5f, 1, 0, 1.0f);
            }
        });
    }
}
