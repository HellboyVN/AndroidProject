package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;

public class ResourceService {
    private static ResourceService INSTANCE = null;

    public static ResourceService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResourceService();
        }
        return INSTANCE;
    }

    private ResourceService() {
    }

    public String getString(String property, Context context) {
        int nameResourceID = context.getResources().getIdentifier(property, "string", context.getApplicationInfo().packageName);
        return nameResourceID == 0 ? null : context.getString(nameResourceID);
    }

    public Drawable getDrawable(String resource, Context context) {
        int nameResourceID = context.getResources().getIdentifier(resource, "drawable", context.getApplicationInfo().packageName);
        return nameResourceID == 0 ? null : ContextCompat.getDrawable(context, nameResourceID);
    }

    public int getdrawableResourceId(String resource, Context context) {
        return context.getResources().getIdentifier(resource, "drawable", context.getApplicationInfo().packageName);
    }

    public Integer getSound(String resource, Context context) {
        int nameResourceID = context.getResources().getIdentifier(resource, "raw", context.getApplicationInfo().packageName);
        return nameResourceID == 0 ? null : Integer.valueOf(nameResourceID);
    }

    public Drawable changeVectorDrawableColor(AppCompatImageView imageView, int color) {
        Drawable drawable;
        if (VERSION.SDK_INT >= 21) {
            drawable = imageView.getDrawable();
        } else {
            drawable = imageView.getDrawable();
        }
        if (drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(color, Mode.SRC_IN));
            if (VERSION.SDK_INT < 16) {
                imageView.setBackgroundDrawable(drawable);
            } else {
                imageView.setBackground(drawable);
            }
        }
        return drawable;
    }

    public static Bitmap getBitmap(Context context, int drawableId) {
        Drawable drawable;
        if (VERSION.SDK_INT <= 21) {
            drawable = ContextCompat.getDrawable(context, drawableId);
            if (VERSION.SDK_INT < 21) {
                drawable = DrawableCompat.wrap(drawable).mutate();
            }
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        }
        throw new IllegalArgumentException("unsupported drawable type");
    }

    @TargetApi(21)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}
