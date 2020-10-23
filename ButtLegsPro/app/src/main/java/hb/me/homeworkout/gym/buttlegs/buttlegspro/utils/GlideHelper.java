package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.content.Context;
import android.os.Build.VERSION;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideHelper {
    public static void loadResource(Context context, ImageView imageView, int resourceId) {
        if (VERSION.SDK_INT <= 19) {
            imageView.setLayerType(1, null);
        }
        Glide.with(context).load(Integer.valueOf(resourceId)).override(300, 360).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().into(imageView);
    }
}
