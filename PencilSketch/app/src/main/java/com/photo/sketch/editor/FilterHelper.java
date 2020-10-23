package com.photo.sketch.editor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import com.pencilsketch.photo.maker.others.BitmapHelper;
import com.sketches.editor.GalleryFileSizeHelper;

public class FilterHelper extends BitmapHelper {
    public FilterHelper(Activity activity, Handler handler) {
        super(activity, handler);
    }

    public Bitmap getSketchFromBH(Bitmap scaleBitmap) {
        return GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0d);
    }
}
