package com.photo.sketch.editor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.SavingHelper;
import com.pencilsketch.photo.maker.others.BitmapHelper;
import com.sketches.editor.ColorLevels;
import com.sketches.editor.GalleryFileSizeHelper;
import com.sketches.editor.SecondSketchFilter;
import com.sketches.editor.ThresoldHelper;

public class SketchFilter extends BitmapHelper {
    private Context d;
    int pencilSketchValue1 = 2;
    int pencilSketchValue2 = 80;

    public SketchFilter(Activity activity, Handler handler) {
        super(activity, handler);
        this.d = activity;
    }

    public Bitmap getSketchFromBH(Bitmap scaleBitmap) throws Throwable {
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0d);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.pencilSketchValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_6, R.drawable.sketch_6), Mode.SCREEN);
        thresoldHelper.setThresholdValue(120);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3, FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5), Mode.SCREEN);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.DARKEN);
        if (!(a2 == null || a2.isRecycled())) {
            a2.recycle();
            System.gc();
        }
        thresoldHelper.setThresholdValue(40);
        a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_1, R.drawable.sketch_1), Mode.SCREEN);
        GalleryFileSizeHelper.DrawRectToBitmap(a2, 50.0f);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, a2, Mode.MULTIPLY);
        bitmap3.recycle();
        Bitmap bitmap4 = SavingHelper.getBitmapScale(this.d, (String) FilterSizeHelper.hashmap2FSH.get(Integer.valueOf(this.pencilSketchValue1)), Math.min(scaleBitmap.getWidth(), scaleBitmap.getHeight()));
        if (bitmap4 == null) {
            SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
            secondSketchFilter.getSimpleSketchValue(this.pencilSketchValue1);
            a = new ColorLevels().getColorLevelBitmap(secondSketchFilter.getSimpleSketch(scaleBitmap));
            GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, a2, Mode.MULTIPLY);
            a.recycle();
            System.gc();
        } else {
            a = new ColorLevels().getColorLevelBitmap(bitmap4);
            GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, a2, Mode.MULTIPLY);
            a.recycle();
            System.gc();
        }
        return a2;
    }
    public Bitmap getSketchFromBHHellboy(Bitmap scaleBitmap) throws Throwable {
        Bitmap a = GalleryFileSizeHelper.scaleItBitmap(scaleBitmap, 1.0d);
        ThresoldHelper thresoldHelper = new ThresoldHelper();
        thresoldHelper.setThresholdValue(this.pencilSketchValue2);
        Bitmap a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_11, R.drawable.sketch_11), Mode.SCREEN);
        thresoldHelper.setThresholdValue(120);
        Bitmap bitmap3 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, bitmap3, FilterSizeHelper.getFSHbitmap(bitmap3, R.drawable.sketch_5, R.drawable.sketch_5), Mode.SCREEN);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(a2, bitmap3, Mode.DARKEN);
        if (!(a2 == null || a2.isRecycled())) {
            a2.recycle();
            System.gc();
        }
        thresoldHelper.setThresholdValue(40);
        a2 = thresoldHelper.getThresholdBitmap(a);
        GalleryFileSizeHelper.MakeBitmapForMerge(this.d, a2, FilterSizeHelper.getFSHbitmap(a2, R.drawable.sketch_10, R.drawable.sketch_10), Mode.SCREEN);
        GalleryFileSizeHelper.DrawRectToBitmap(a2, 50.0f);
        GalleryFileSizeHelper.mergeBitmapWithPorterMode(bitmap3, a2, Mode.MULTIPLY);
        bitmap3.recycle();
        Bitmap bitmap4 = SavingHelper.getBitmapScale(this.d, (String) FilterSizeHelper.hashmap2FSH.get(Integer.valueOf(this.pencilSketchValue1)), Math.min(scaleBitmap.getWidth(), scaleBitmap.getHeight()));
        if (bitmap4 == null) {
            SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
            secondSketchFilter.getSimpleSketchValue(this.pencilSketchValue1);
            a = new ColorLevels().getColorLevelBitmap(secondSketchFilter.getSimpleSketch(scaleBitmap));
            GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, a2, Mode.MULTIPLY);
            a.recycle();
            System.gc();
        } else {
            a = new ColorLevels().getColorLevelBitmap(bitmap4);
            GalleryFileSizeHelper.mergeBitmapWithPorterMode(a, a2, Mode.MULTIPLY);
            a.recycle();
            System.gc();
        }
        return a2;
    }
}
