package com.sketches.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.io.IOException;
import java.io.InputStream;

public final class GalleryFileSizeHelper {
    public static void mergeBitmapWithPorterMode(Bitmap bitmap, Bitmap bitmap2, Mode mode) {
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap2);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(mode));
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), paint);
    }

    public static void MakeBitmapForMerge(Context context, Bitmap bitmap, int i, Mode mode) throws Throwable {
        Bitmap createBitmap;
        Bitmap createBitmap2;
        Throwable th;
        Options options = new Options();
        options.inPreferredConfig = Config.RGB_565;
        InputStream openRawResource = context.getResources().openRawResource(i);
        Bitmap decodeStream = BitmapFactory.decodeStream(openRawResource, null, options);
        try {
            openRawResource.close();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
            createBitmap = Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(), decodeStream.getHeight(), matrix, true);
        } else {
            createBitmap = decodeStream;
        }
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int width2 = createBitmap.getWidth();
            int height2 = createBitmap.getHeight();
            float max = Math.max(((float) width) / ((float) width2), ((float) height) / ((float) height2));
            float f = ((float) width2) * max;
            float f2 = ((float) height2) * max;
            max = (((float) width) - f) / 2.0f;
            float f3 = (((float) height) - f2) / 2.0f;
            RectF rectF = new RectF(max, f3, f + max, f2 + f3);
            createBitmap2 = Bitmap.createBitmap(width, height, Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap2);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            canvas.drawBitmap(createBitmap, null, rectF, null);
            createBitmap.recycle();
            try {
                rect = new Rect(0, 0, createBitmap2.getWidth(), createBitmap2.getHeight());
                Canvas canvas2 = new Canvas();
                canvas2.setBitmap(bitmap);
                Paint paint = new Paint();
                paint.setXfermode(new PorterDuffXfermode(mode));
                paint.setAntiAlias(true);
                paint.setFilterBitmap(true);
                canvas2.drawBitmap(createBitmap2, rect, rect, paint);
                if (!(createBitmap2 == null || createBitmap2.isRecycled())) {
                    createBitmap2.recycle();
                }
                System.gc();
            } catch (NullPointerException e) {
                createBitmap2.recycle();
                System.gc();
            } catch (Exception e5) {
                createBitmap = createBitmap2;
                e5.printStackTrace();
                createBitmap.recycle();
                System.gc();
            } catch (Throwable th3) {
                th = th3;
                createBitmap2 = createBitmap;
                if (createBitmap2 != null) {
                    if (!createBitmap2.isRecycled()) {
                        createBitmap2.recycle();
                    }
                }
                System.gc();
            }
        } catch (NullPointerException e2) {
            createBitmap2 = createBitmap;
            if (!(createBitmap2 == null || createBitmap2.isRecycled())) {
                createBitmap2.recycle();
            }
            System.gc();
        } catch (Exception e7) {
            e7.printStackTrace();
            if (!(createBitmap == null || createBitmap.isRecycled())) {
                createBitmap.recycle();
            }
            System.gc();
        }
    }

    public static Bitmap createScaledBitmapForMerge(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int min = Math.min(width, height);
        if (min <= i) {
            return bitmap;
        }
        double d = (((double) min) * 1.0d) / ((double) i);
        return Bitmap.createScaledBitmap(bitmap, (int) Math.floor(((double) width) / d), (int) Math.floor(((double) height) / d), true);
    }

    public static Bitmap scaleItBitmap(Bitmap bitmap, double d) {
        return d <= 1.0d ? bitmap : Bitmap.createScaledBitmap(bitmap, (int) Math.floor(((double) bitmap.getWidth()) / d), (int) Math.floor(((double) bitmap.getHeight()) / d), true);
    }

    public static void DrawRectToBitmap(Bitmap bitmap, float f) {
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.argb((int) ((((double) f) / 100.0d) * 255.0d), 255, 255, 255));
        canvas.drawRect(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), paint);
    }

    public static Bitmap getCreatedBitmap(Bitmap bitmap) {
        int[] mArray = new int[(bitmap.getWidth() * bitmap.getHeight())];
        bitmap.getPixels(mArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < mArray.length; i++) {
            mArray[i] = 2130706432 | (mArray[i] & ViewCompat.MEASURED_SIZE_MASK);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        createBitmap.setPixels(mArray, 0, createBitmap.getWidth(), 0, 0, createBitmap.getWidth(), createBitmap.getHeight());
        return createBitmap;
    }

    public static Bitmap getCreatedBitmapWithMatrix(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix();
        setColorMatrixToBitmap(colorMatrix, 10.0f);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, 0.0f, paint);
        bitmap.recycle();
        return createBitmap;
    }

    public static void setColorMatrixToBitmap(ColorMatrix colorMatrix, float f) {
        float min = (Math.min(BitmapDescriptorFactory.HUE_CYAN, Math.max(-180.0f, f)) / BitmapDescriptorFactory.HUE_CYAN) * 3.1415927f;
        if (min != 0.0f) {
            float[] r2 = new float[25];
            r2[12] = (float) ((Math.sin((double) min) * 0.07199999690055847d) + ((double) ((0.928f * ((float) Math.cos((double) min))) + 0.072f)));
            r2[13] = 0.0f;
            r2[14] = 0.0f;
            r2[15] = 0.0f;
            r2[16] = 0.0f;
            r2[17] = 0.0f;
            r2[18] = TextTrackStyle.DEFAULT_FONT_SCALE;
            r2[19] = 0.0f;
            r2[20] = 0.0f;
            r2[21] = 0.0f;
            r2[22] = 0.0f;
            r2[23] = 0.0f;
            r2[24] = TextTrackStyle.DEFAULT_FONT_SCALE;
            colorMatrix.postConcat(new ColorMatrix(r2));
        }
    }

    public static float setScaleOrColorValue(int i) {
        if (i <= 0) {
            return 2.0f;
        }
        if (i > 0 && i <= 20) {
            return 2.4f;
        }
        if (i > 20 && i <= 50) {
            return 2.8f;
        }
        if (i <= 50 || i > 80) {
            return 4.0f;
        }
        return 3.4f;
    }

    public static Bitmap getFilterBitmapMine(Bitmap bitmap, int i) {
        float a = (float) (((((double) i) + 100.0d) / 200.0d) * ((double) setScaleOrColorValue(i)));
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.reset();
        colorMatrix.setSaturation(a);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }
}
