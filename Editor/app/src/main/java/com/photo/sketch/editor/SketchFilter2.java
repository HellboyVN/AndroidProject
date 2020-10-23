package com.photo.sketch.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import com.aerotools.photo.sketch.maker.editor.R;
import com.aerotools.photo.sketch.maker.editor.SavingHelper;
import com.pencilsketch.photo.maker.others.BitmapHelper;
import com.sketches.editor.ColorLevels;
import com.sketches.editor.FirstSketchFilter;
import com.sketches.editor.GalleryFileSizeHelper;
import com.sketches.editor.ThresoldHelper;

public class SketchFilter2 extends BitmapHelper {
    private Context d;
    int penci2SketchValue1 = 6;
    int penci2SketchValue2 = 70;

    public SketchFilter2(Activity activity, Handler handler) {
        super(activity, handler);
        this.d = activity;
    }

    public Bitmap getSketchFromBH(Bitmap scaleBitmap) throws Throwable {
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0d);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.penci2SketchValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_3, R.drawable.sketch_3), Mode.SCREEN);
        thresoldHelper.setThresholdValue(140);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3, FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5), Mode.SCREEN);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.DARKEN);
        a2.recycle();
        Bitmap bitmap4 = SavingHelper.getBitmapScale(this.d, (String) FilterSizeHelper.hashmap1FSH.get(Integer.valueOf(this.penci2SketchValue1)), Math.min(scaleBitmap.getWidth(), scaleBitmap.getHeight()));
        if (bitmap4 == null) {
            FirstSketchFilter firstSketchFilter = new FirstSketchFilter();
            firstSketchFilter.setSketchValue(this.penci2SketchValue1);
            a = new ColorLevels().getColorLevelBitmap(firstSketchFilter.getFirstSketch(scaleBitmap));
            GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, bitmap3, Mode.MULTIPLY);
            a.recycle();
            System.gc();
        } else {
            a = new ColorLevels().getColorLevelBitmap(bitmap4);
            GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, bitmap3, Mode.MULTIPLY);
            a.recycle();
            System.gc();
        }
        return bitmap3;
    }
}
