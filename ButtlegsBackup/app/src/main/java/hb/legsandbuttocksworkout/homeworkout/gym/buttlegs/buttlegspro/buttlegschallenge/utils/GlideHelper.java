package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v7.widget.AppCompatImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideHelper {
    public static void loadResource(Context context, AppCompatImageView imageView, int resourceId) {
        if (VERSION.SDK_INT <= 19) {
            imageView.setLayerType(1, null);
        }
        Glide.with(context).load(Integer.valueOf(resourceId)).override(300, 360).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(imageView);
    }
}
