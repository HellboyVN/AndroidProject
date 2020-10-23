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

public class SketchColorFilter extends BitmapHelper {
    int colorPencilValue = 2;
    int colorPencilValue2 = 80;
    private Context d;

    public SketchColorFilter(Activity activity, Handler handler) {
        super(activity, handler);
        this.d = activity;
    }

    public Bitmap getSketchFromBH(Bitmap scaleBitmap) throws Throwable {
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0d);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.colorPencilValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_7, R.drawable.sketch_7), Mode.SCREEN);
        thresoldHelper.setThresholdValue(120);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3, FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_8, R.drawable.sketch_8), Mode.SCREEN);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.DARKEN);
        if (!(a2 == null || a2.isRecycled())) {
            a2.recycle();
            System.gc();
        }
        thresoldHelper.setThresholdValue(40);
        Bitmap bitmap4 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap4, FilterSizeHelper.getFSHbitmap(bitmap4, R.drawable.sketch_1, R.drawable.sketch_1), Mode.SCREEN);
        GalleryFileSizeHelper.DrawRectToBitmap(bitmap4, 50.0f);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, bitmap4, Mode.MULTIPLY);
        if (!(bitmap3 == null || bitmap3.isRecycled())) {
            bitmap3.recycle();
            System.gc();
        }
        if (SavingHelper.getBitmapScale(this.d, (String) FilterSizeHelper.hashmap2FSH.get(Integer.valueOf(this.colorPencilValue)), Math.min(scaleBitmap.getWidth(), scaleBitmap.getHeight())) != null) {
            return null;
        }
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(new ColorLevels().getColorLevelBitmap(new SecondSketchFilter().getSimpleSketch(scaleBitmap)), bitmap4, Mode.MULTIPLY);
        a2 = GalleryFileSizeHelper.getFilterBitmapMine(a, 90);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap4, a2, Mode.SCREEN);
        if (!(bitmap4 == null || bitmap4.isRecycled())) {
            bitmap4.recycle();
            System.gc();
        }
        System.gc();
        return a2;
    }
}
