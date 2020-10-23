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
import com.sketches.editor.GalleryFileSizeHelper;
import com.sketches.editor.SecondSketchFilter;
import com.sketches.editor.ThresoldHelper;

public class CSketchFilter extends BitmapHelper {
    int comicIntValue1 = 2;
    int comicIntValue2 = 90;
    private Context d;

    public CSketchFilter(Activity activity, Handler handler) {
        super(activity, handler);
        this.d = activity;
    }

    public Bitmap getSketchFromBH(Bitmap scaleBitmap) throws Throwable {
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0d);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.comicIntValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_9, R.drawable.sketch_9), Mode.SCREEN);
        thresoldHelper.setThresholdValue(30);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.MULTIPLY);
        a2.recycle();
        Bitmap bitmap4 = SavingHelper.getBitmapScale(this.d, (String) FilterSizeHelper.hashmap2FSH.get(Integer.valueOf(this.comicIntValue1)), Math.min(scaleBitmap.getWidth(), scaleBitmap.getHeight()));
        if (bitmap4 == null) {
            SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
            secondSketchFilter.getSimpleSketchValue(this.comicIntValue1);
            a = new ColorLevels().getColorLevelBitmap(secondSketchFilter.getSimpleSketch(scaleBitmap));
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
