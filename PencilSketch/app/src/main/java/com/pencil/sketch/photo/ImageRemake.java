package com.pencil.sketch.photo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.AppConstant;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.edmodo.cropper.CropImageView;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;
import com.pencilsketch.photo.maker.others.ActivityHandler;
import com.pencilsketch.photo.maker.others.BitmapHelper;
import com.photo.sketch.editor.CSketchFilter;
import com.photo.sketch.editor.FilterHelper;
import com.photo.sketch.editor.SketchColorFilter;
import com.photo.sketch.editor.SketchColorFilter2;
import com.photo.sketch.editor.SketchFilter;
import com.photo.sketch.editor.SketchFilter2;
import com.sketches.editor.SecondSketchFilter;

import net.alhazmy13.imagefilter.ImageFilter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.UUID;


public class ImageRemake extends Activity implements OnClickListener, AnimationListener, BillingProcessor.IBillingHandler {

    public static int overlayid = -1;
    public static Bitmap pic_forDraw;
    public static Bitmap pic_forSketch;
    public static Bitmap pic_result;
    String Imagepath;
    int MaxResolution;
    int random = 0;
    boolean MoveBack = false;
    Float Orientation;
    ActivityHandler activityHelper;
    Animation anim_bottom_show;
    Animation anim_btnapply;
    Animation animhidebtn;
    Animation animsgallerybtn;
    Animation animshowbtndown;
    Animation animshowbtnup;
    RelativeLayout hell1;
    ImageButton RemoveAds;
    AppConstant appConstant;
    BillingProcessor bp;
    String[] as = new String[]{"Filter 1", "Filter 2", "Filter 3",
            "Filter 4", "Filter 5", "Filter 6",
            "Filter 7", "Filter 8", "Filter 9",
            "Filter 10","Filter 11","Filter 12","Random"};
    Canvas bitmapCanvas;
    BitmapHelper bitmapHelper;
    Bitmap colorPencil2Bitmap;
    Bitmap colorPencilBitmap = null;
    Bitmap comicBitmap = null;
    Bitmap randomwBitmap = null;
    LinearLayout crop_gallery;
    int currentapiVersion;
    Boolean effapplied;
    Integer[] effectImages = new Integer[]{Integer.valueOf(R.drawable.pic_eff_0),
            Integer.valueOf(R.drawable.pic_eff_1), Integer.valueOf(R.drawable.pic_eff_2),
            Integer.valueOf(R.drawable.pic_eff_3), Integer.valueOf(R.drawable.pic_eff_4),
            Integer.valueOf(R.drawable.pic_eff_5), Integer.valueOf(R.drawable.pic_eff_6),
            Integer.valueOf(R.drawable.pic_eff_7), Integer.valueOf(R.drawable.pic_eff_8),
            Integer.valueOf(R.drawable.pic_eff_9),Integer.valueOf(R.drawable.pic_eff_10),
            Integer.valueOf(R.drawable.pic_eff_11),Integer.valueOf(R.drawable.pic_eff_12)};
    LinearLayout effect_gallery;
    Dialog exit_dialog;
    boolean filterApplyed = false;
    RelativeLayout gallery_layout;
    Bitmap grainBitmap = null;
    int imageheight;
    Uri imageuri;
    int imagewidth;
    int intensity = 1;
    boolean lineOne = true;
    boolean moveforword = true;
    Bitmap pencil2Bitmap = null;
    Bitmap pencilsketchBitmap = null;
    Bitmap pencilsketchBitmapHellboy = null;
    LinearLayout pic_apply_layout;
    LinearLayout pic_btn_gallery;
    CropImageView pic_cropImageView;
    LinearLayout pic_donelayout;
    ImageView pic_imageview;
    int screenheight;
    int screenwidth;
    LinearLayout seek_layout;
    String sendimagepath;
    Bitmap simpleSketchbitmap1 = null;
    Bitmap simpleSketchbitmap2 = null;
    boolean sketchDone = false;
    String[] tool_array;
    TextView txt_editor;
    FrameLayout viewContainer;


    public class BlurView extends View {
        int Tilltime = 0;
        Bitmap bitmap;
        Bitmap bmOverlay;
        Context context;
        int mHeight;
        PointF mImagePos = new PointF();
        PointF mImageSource = new PointF();
        PointF mImageTarget = new PointF();
        long mInterpolateTime;
        private Paint mPaint;
        int mWidth;
        Canvas pcanvas;
        int r = 0;
        int x = 0;
        int y = 0;

        public BlurView(Context context) {
            super(context);
            this.context = context;
            setFocusable(true);
            setBackgroundColor(0);
            this.mPaint = new Paint(1);
            this.mPaint.setColor(0);
            this.mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_OUT));
            this.mPaint.setAntiAlias(true);
            ImageRemake.this.getWindowManager().getDefaultDisplay().getSize(new Point());
            this.bmOverlay = Bitmap.createBitmap(ImageRemake.pic_forDraw.getWidth(), ImageRemake.pic_forDraw.getHeight(), Config.ARGB_8888);
            this.pcanvas = new Canvas(this.bmOverlay);
            ImageRemake.pic_forDraw.eraseColor(-1);
        }

        @SuppressLint({"DrawAllocation"})
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawBitmap(ImageRemake.pic_result, 0.0f, 0.0f, null);
            this.pcanvas.drawBitmap(ImageRemake.pic_forDraw, 0.0f, 0.0f, null);
            this.pcanvas.drawCircle(this.mImagePos.x, this.mImagePos.y, (float) (ImageRemake.pic_result.getHeight() / 20), this.mPaint);
            update();
            invalidate();
            canvas.drawBitmap(this.bmOverlay, 0.0f, 0.0f, null);
            ImageRemake.this.bitmapCanvas = new Canvas(ImageRemake.pic_forDraw);
            ImageRemake.this.bitmapCanvas.drawBitmap(ImageRemake.pic_result, 0.0f, 0.0f, null);
            ImageRemake.this.bitmapCanvas.drawBitmap(this.bmOverlay, 0.0f, 0.0f, null);
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.drawing_hand), this.mImagePos.x, this.mImagePos.y, null);
            ImageRemake.this.intensity = 20;
        }

        public void update() {
            if (this.Tilltime < 400) {
                long time = SystemClock.uptimeMillis();
                if (time - this.mInterpolateTime > 200) {
                    this.mImageSource.set(this.mImageTarget);
                    this.mImageTarget.x = (float) (Math.random() * ((double) ImageRemake.pic_forDraw.getWidth()));
                    this.mImageTarget.y = (float) (Math.random() * ((double) ImageRemake.pic_forDraw.getHeight()));
                    this.mInterpolateTime = time;
                }
                float t = ((float) (time - this.mInterpolateTime)) / 200.0f;
                t = (t * t) * (3.0f - (2.0f * t));
                this.mImagePos.x = this.mImageSource.x + ((this.mImageTarget.x - this.mImageSource.x) * t);
                this.mImagePos.y = this.mImageSource.y + ((this.mImageTarget.y - this.mImageSource.y) * t);
                this.Tilltime++;
                return;
            }
            if (ImageRemake.this.lineOne) {
                this.mImagePos.y = 0.0f;
                ImageRemake.this.lineOne = false;
            }
            if (this.mImagePos.y <= ((float) ImageRemake.pic_forDraw.getHeight())) {
                PointF pointF;
                if (this.mImagePos.x <= 0.0f) {
                    ImageRemake.this.moveforword = true;
                    ImageRemake.this.MoveBack = false;
                    pointF = this.mImagePos;
                    pointF.y += (float) (ImageRemake.pic_result.getHeight() / 20);
                } else if (this.mImagePos.x >= ((float) ImageRemake.pic_forDraw.getWidth())) {
                    ImageRemake.this.moveforword = false;
                    ImageRemake.this.MoveBack = true;
                    pointF = this.mImagePos;
                    pointF.y += (float) (ImageRemake.pic_result.getHeight() / 20);
                }
                if (ImageRemake.this.moveforword) {
                    pointF = this.mImagePos;
                    pointF.x += (float) (ImageRemake.pic_forDraw.getWidth() / 20);
                    return;
                } else if (ImageRemake.this.MoveBack) {
                    pointF = this.mImagePos;
                    pointF.x -= (float) (ImageRemake.pic_result.getWidth() / 20);
                    return;
                } else {
                    return;
                }
            }
            ImageRemake.this.sketchDone = true;
            ImageRemake.this.viewContainer.setVisibility(4);
            ImageRemake.this.pic_imageview.setVisibility(View.VISIBLE);
            ImageRemake.this.pic_imageview.setImageBitmap(ImageRemake.pic_result);
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(ImageRemake.pic_result.getWidth(), ImageRemake.pic_result.getHeight());
        }

        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case 0:
                    this.x = (int) ev.getX();
                    this.y = (int) ev.getY();
                    invalidate();
                    break;
                case 2:
                    this.x = (int) ev.getX();
                    this.y = (int) ev.getY();
                    invalidate();
                    break;
            }
            return true;
        }
    }

    public class LoadImageAsycTask extends AsyncTask<Void, Void, Void> {
        Float Orientation;
        ProgressDialog dialog;
        Boolean getimage = Boolean.valueOf(false);

        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(ImageRemake.this, "", "Loading...");
            this.dialog.setCancelable(false);
            ImageRemake.this.getIntentExtra();
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            if (ImageRemake.this.imageuri == null) {
                try {
                    this.getimage = Boolean.valueOf(false);
                } catch (OutOfMemoryError e) {
                    this.getimage = Boolean.valueOf(false);
                } catch (NullPointerException e2) {
                    this.getimage = Boolean.valueOf(false);
                } catch (Exception e3) {
                    this.getimage = Boolean.valueOf(false);
                }
                this.getimage = Boolean.valueOf(false);
            }
            ImageRemake.this.Imagepath = ImageRemake.this.getRealPathFromURI(ImageRemake.this.imageuri);
            if ((ImageRemake.this.Imagepath != null && ImageRemake.this.Imagepath.endsWith(".png")) || ImageRemake.this.Imagepath.endsWith(".jpg") || ImageRemake.this.Imagepath.endsWith(".jpeg") || ImageRemake.this.Imagepath.endsWith(".bmp")) {
                this.Orientation = Float.valueOf(ImageRemake.this.getImageOrientation(ImageRemake.this.Imagepath));
                ImageRemake.this.getAspectRatio(ImageRemake.this.Imagepath, (float) ImageRemake.this.MaxResolution);
                ImageRemake.pic_result = ImageRemake.this.getResizedOriginalBitmap(ImageRemake.this.Imagepath, this.Orientation.floatValue());
                ImageRemake.pic_forSketch = ImageRemake.this.getResizedOriginalBitmap(ImageRemake.this.Imagepath, this.Orientation.floatValue());
                ImageRemake.pic_forDraw = ImageRemake.this.getResizedOriginalBitmap(ImageRemake.this.Imagepath, this.Orientation.floatValue());
                this.getimage = Boolean.valueOf(true);
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            ImageRemake.this.gallery_layout.setVisibility(View.VISIBLE);
            if (!this.getimage.booleanValue()) {
                Toast.makeText(ImageRemake.this.getApplicationContext(), "Unsupported media file.", 0).show();
                ImageRemake.this.finish();
            } else if (ImageRemake.pic_result == null || ImageRemake.pic_result.getHeight() <= 5 || ImageRemake.pic_result.getWidth() <= 5) {
                Toast.makeText(ImageRemake.this.getApplicationContext(), "Image Format not supported .", 0).show();
                ImageRemake.this.finish();
            } else {
                ImageRemake.this.pic_imageview.setImageBitmap(ImageRemake.pic_result);
                ImageRemake.this.stringMatching();
            }
            this.dialog.dismiss();
            super.onPostExecute(result);
        }
    }

    public class SketchAsnyTask extends AsyncTask<Integer, Void, Void> {
        Bitmap bitmap = this.eff;
        ProgressDialog dialogD;
        Bitmap eff;

        protected void onPreExecute() {
            this.dialogD = ProgressDialog.show(ImageRemake.this, "", "Drawing...");
            this.dialogD.setCancelable(false);
            super.onPreExecute();
        }

        protected Void doInBackground(Integer... params) {
            this.eff = ImageRemake.this.getSketchBitmap(ImageRemake.pic_forSketch, params[0].intValue());
            return null;
        }

        protected void onPostExecute(Void result) {
            ImageRemake.pic_result = this.eff;
            ImageRemake.this.pic_imageview.setImageBitmap(ImageRemake.pic_result);
            this.dialogD.dismiss();
            if (!(this.bitmap == null || this.bitmap.isRecycled())) {
                this.bitmap.recycle();
                System.gc();
            }
            super.onPostExecute(result);
        }
    }

    public class SketchAsnyTaskFirst extends AsyncTask<Integer, Void, Void> {
        Bitmap bitmap = this.eff;
        ProgressDialog dialogD;
        Bitmap eff;

        protected void onPreExecute() {
            this.dialogD = ProgressDialog.show(ImageRemake.this, "", "Drawing...");
            this.dialogD.setCancelable(false);
            super.onPreExecute();
        }

        protected Void doInBackground(Integer... params) {
            Log.e("Hellboy ","type "+params[0].intValue());
            this.eff = ImageRemake.this.getSketchBitmap(ImageRemake.pic_forDraw, params[0].intValue());
            return null;
        }

        protected void onPostExecute(Void result) {
            ImageRemake.pic_result = this.eff;
            ImageRemake.this.viewContainer.setVisibility(View.VISIBLE);
            ImageRemake.this.viewContainer.addView(new BlurView(ImageRemake.this));
            ImageRemake.pic_result = this.eff;
            ImageRemake.this.pic_imageview.setVisibility(4);
            this.dialogD.dismiss();
            if (!(this.bitmap == null || this.bitmap.isRecycled())) {
                this.bitmap.recycle();
                System.gc();
            }
            super.onPostExecute(result);
        }
    }

    public ImageRemake() {
        pic_result = null;
        this.imageuri = null;
        this.Imagepath = null;
        this.effapplied = Boolean.valueOf(false);
        this.tool_array = null;
        this.activityHelper = new ActivityHandler(this, this);
        this.bitmapHelper = new FilterHelper(this, this.activityHelper);
    }

    public Bitmap getSketchBitmap(Bitmap bm1, int type) {
        Bitmap SketchBitmap = bm1;
        switch (type) {
            case 0:
                SketchColorFilter2 sketchColorFilter2 = new SketchColorFilter2(this, this.activityHelper);
                if (this.colorPencil2Bitmap != null) {
                    return this.colorPencil2Bitmap;
                }
                try {
                    this.colorPencil2Bitmap = sketchColorFilter2.getSketchFromBH(bm1);
                    return this.colorPencil2Bitmap;
                } catch (Throwable e) {
                    e.printStackTrace();
                    return SketchBitmap;
                }
            case 1:
                SecondSketchFilter secondSketchFilter = new SecondSketchFilter();
                if (this.simpleSketchbitmap2 != null) {
                    return this.simpleSketchbitmap2;
                }
                try {
                    this.simpleSketchbitmap2 = secondSketchFilter.getSimpleSketch(bm1);
                    return this.simpleSketchbitmap2;
                } catch (Throwable e222222) {
                    e222222.printStackTrace();
                    return SketchBitmap;
                }
            case 2:
                SketchColorFilter printFilter = new SketchColorFilter(this, this.activityHelper);
                if (this.colorPencilBitmap != null) {
                    return this.colorPencilBitmap;
                }
                try {
                    this.colorPencilBitmap = printFilter.getSketchFromBH(bm1);
                    return this.colorPencilBitmap;
                } catch (Throwable e22) {
                    e22.printStackTrace();
                    return SketchBitmap;
                }
            case 3:
                SketchFilter2 sketchFilter2 = new SketchFilter2(this, this.activityHelper);
                if (this.pencil2Bitmap != null) {
                    return this.pencil2Bitmap;
                }
                try {
                    this.pencil2Bitmap = sketchFilter2.getSketchFromBH(bm1);
                    return this.pencil2Bitmap;
                } catch (Throwable e222) {
                    e222.printStackTrace();
                    return SketchBitmap;
                }
            case 4:
                CSketchFilter cSketchFilter = new CSketchFilter(this, this.activityHelper);
                if (this.comicBitmap != null) {
                    return this.comicBitmap;
                }
                try {
                    this.comicBitmap = cSketchFilter.getSketchFromBH(bm1);
                    return this.comicBitmap;
                } catch (Throwable e2222) {
                    e2222.printStackTrace();
                    return SketchBitmap;
                }
            case 5:
                if (this.simpleSketchbitmap1 != null) {
                    return this.simpleSketchbitmap1;
                }
                try {
                    Bitmap greyScaleCoppy = toGrayscale(bm1);
                    Bitmap invertCopy = invert(greyScaleCoppy);
                    Bitmap blurImage = fastblur(invertCopy, 7);
                    if (!(invertCopy == null || invertCopy.isRecycled())) {
                        invertCopy.recycle();
                        System.gc();
                    }
                    this.simpleSketchbitmap1 = ColorDodgeBlend(blurImage, greyScaleCoppy);
                    if (!(greyScaleCoppy == null || greyScaleCoppy.isRecycled())) {
                        greyScaleCoppy.recycle();
                        System.gc();
                    }
                    if (!(blurImage == null || blurImage.isRecycled())) {
                        blurImage.recycle();
                        System.gc();
                    }
                    this.simpleSketchbitmap1 = toGrayscale(this.simpleSketchbitmap1);
                    return this.simpleSketchbitmap1;
                } catch (Throwable e22222) {
                    e22222.printStackTrace();
                    return SketchBitmap;
                }
            case 6:
                SketchFilter sketchFilter = new SketchFilter(this, this.activityHelper);
                if (this.pencilsketchBitmap != null) {
                    return this.pencilsketchBitmap;
                }
                try {
                    this.pencilsketchBitmap = sketchFilter.getSketchFromBH(bm1);
                    return this.pencilsketchBitmap;
                } catch (Throwable e2) {
                    e2.printStackTrace();
                    return SketchBitmap;
                }
            case 7:
                if (this.simpleSketchbitmap2 != null) {
                    return ConvertToSepia(this.simpleSketchbitmap2);
                }
                try {
                    this.simpleSketchbitmap2 = new SecondSketchFilter().getSimpleSketch(bm1);
                    return ConvertToSepia(this.simpleSketchbitmap2);
                } catch (Throwable e2222222) {
                    e2222222.printStackTrace();
                    return SketchBitmap;
                }
            case 8:
                return ImageFilter.applyFilter(bm1, ImageFilter.Filter.OIL);
            case 9:
                return ImageFilter.applyFilter(bm1, ImageFilter.Filter.TV);
            case 10:
                return ImageFilter.applyFilter(bm1, ImageFilter.Filter.LIGHT);
            case 11:
                return ImageFilter.applyFilter(bm1, ImageFilter.Filter.PIXELATE);
            case 12:
                SketchFilter sketchFilter1 = new SketchFilter(this, this.activityHelper);
                if (this.pencilsketchBitmapHellboy != null) {
                    return this.pencilsketchBitmapHellboy;
                }
                try {
                    this.pencilsketchBitmapHellboy = sketchFilter1.getSketchFromBHHellboy(bm1);
                    return this.pencilsketchBitmapHellboy;
                } catch (Throwable e2) {
                    e2.printStackTrace();
                    return SketchBitmap;
                }

            default:
                return SketchBitmap;
        }
    }

    public Bitmap ConvertToSepia(Bitmap sampleBitmap) {
        float[] sepMat = new float[]{0.393f, 0.769f, 0.189f, 0.0f, 0.0f, 0.349f, 0.686f, 0.168f, 0.0f, 0.0f, 0.272f, 0.534f, 0.131f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE};
        ColorMatrix sepiaMatrix = new ColorMatrix();
        sepiaMatrix.set(sepMat);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(sepiaMatrix);
        Bitmap rBitmap = sampleBitmap.copy(Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        new Canvas(rBitmap).drawBitmap(rBitmap, 0.0f, 0.0f, paint);
        return rBitmap;
    }

    public Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public Bitmap invert(Bitmap original) {
        Bitmap inversion = original.copy(Config.ARGB_8888, true);
        int width = inversion.getWidth();
        int height = inversion.getHeight();
        int pixels = width * height;
        int[] pixel = new int[pixels];
        inversion.getPixels(pixel, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels; i++) {
            pixel[i] = pixel[i] ^ ViewCompat.MEASURED_SIZE_MASK;
        }
        inversion.setPixels(pixel, 0, width, 0, 0, width, height);
        return inversion;
    }

    public Bitmap ColorDodgeBlend(Bitmap source, Bitmap layer) {
        Bitmap base = source.copy(Config.ARGB_8888, true);
        Bitmap blend = layer.copy(Config.ARGB_8888, false);
        IntBuffer buffBase = IntBuffer.allocate(base.getWidth() * base.getHeight());
        base.copyPixelsToBuffer(buffBase);
        buffBase.rewind();
        IntBuffer buffBlend = IntBuffer.allocate(blend.getWidth() * blend.getHeight());
        blend.copyPixelsToBuffer(buffBlend);
        buffBlend.rewind();
        IntBuffer buffOut = IntBuffer.allocate(base.getWidth() * base.getHeight());
        buffOut.rewind();
        while (buffOut.position() < buffOut.limit()) {
            int filterInt = buffBlend.get();
            int srcInt = buffBase.get();
            int redValueFilter = Color.red(filterInt);
            int greenValueFilter = Color.green(filterInt);
            int blueValueFilter = Color.blue(filterInt);
            int redValueSrc = Color.red(srcInt);
            int greenValueSrc = Color.green(srcInt);
            int blueValueSrc = Color.blue(srcInt);
            buffOut.put(Color.argb(255, colordodge(redValueFilter, redValueSrc), colordodge(greenValueFilter, greenValueSrc), colordodge(blueValueFilter, blueValueSrc)));
        }
        buffOut.rewind();
        base.copyPixelsFromBuffer(buffOut);
        blend.recycle();
        return base;
    }

    private int colordodge(int in1, int in2) {
        float image = (float) in2;
        float mask = (float) in1;
        if (image != 255.0f) {
            image = Math.min(255.0f, ((float) (((long) mask) << 8)) / (255.0f - image));
        }
        return (int) image;
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal) {
        Bitmap bmpGrayscale = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(bmpOriginal, 0.0f, 0.0f, paint);
        return bmpGrayscale;
    }

    protected void onCreate(Bundle savedInstanceState) {
        boolean flag;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picremake_main);
        bp = new BillingProcessor(this, AppConstant.ggpublishkey, this);
        hell1 = (RelativeLayout)findViewById(R.id.hell1);
        RemoveAds = (ImageButton)findViewById(R.id.image_removeads);
        RemoveAds.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    bp.purchase(ImageRemake.this, "truonglevan");//android.test.purchased
                } catch (ActivityNotFoundException e3) {
                    Toast.makeText(ImageRemake.this, e3.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (AppConstant.isRemoveAds(getApplicationContext())) {
            RemoveAds.setVisibility(View.INVISIBLE);
        }
        appConstant = new AppConstant(this);
        appConstant.adBanner(hell1);
        if (((ActivityManager) getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072) {
            flag = true;
        } else {
            flag = false;
        }
        if (!flag) {
            Toast.makeText(getApplicationContext(), "Editor is not supported in this device.", 0).show();
            finish();
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        this.screenheight = displaymetrics.heightPixels;
        this.screenwidth = displaymetrics.widthPixels;
        this.MaxResolution = this.screenwidth;
        this.viewContainer = (FrameLayout) findViewById(R.id.viewContainer);
        this.pic_btn_gallery = (LinearLayout) findViewById(R.id.btn_gallery);
        this.pic_btn_gallery.setVisibility(View.VISIBLE);
        this.pic_donelayout = (LinearLayout) findViewById(R.id.pic_done_layout);
        this.pic_apply_layout = (LinearLayout) findViewById(R.id.pic_apply_layout);
        this.gallery_layout = (RelativeLayout) findViewById(R.id.gallery_layout);
        this.gallery_layout.setVisibility(View.GONE);
        this.pic_donelayout.setVisibility(View.VISIBLE);
        this.pic_apply_layout.setVisibility(View.GONE);
        this.effect_gallery = (LinearLayout) findViewById(R.id.effect_gallery);
        this.crop_gallery = (LinearLayout) findViewById(R.id.crop_gallery);
        this.pic_cropImageView = (CropImageView) findViewById(R.id.pic_CropImageView);
        this.pic_cropImageView.setGuidelines(1);
        this.pic_cropImageView.setImageResource(0);
        this.pic_cropImageView.setVisibility(View.GONE);
        this.effect_gallery.setVisibility(View.GONE);
        this.crop_gallery.setVisibility(View.GONE);
        this.txt_editor = (TextView) findViewById(R.id.pic_txteditor);
        this.txt_editor.setText("EDITOR");
        this.seek_layout = (LinearLayout) findViewById(R.id.seekbarlayout);
        this.seek_layout.setVisibility(View.GONE);
        this.pic_imageview = (ImageView) findViewById(R.id.iv_imagemaker);
        this.pic_donelayout.setOnClickListener(this);
        this.pic_apply_layout.setOnClickListener(this);
        this.currentapiVersion = VERSION.SDK_INT;
        new LoadImageAsycTask().execute(new Void[0]);
        this.animhidebtn = AnimationUtils.loadAnimation(this, R.anim.hide_button_anims);
        this.animsgallerybtn = AnimationUtils.loadAnimation(this, R.anim.rightleft_gallery_anims);
        this.animshowbtnup = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_up);
        this.anim_btnapply = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_up);
        this.animshowbtndown = AnimationUtils.loadAnimation(this, R.anim.show_button_anims_down);
        this.anim_bottom_show = AnimationUtils.loadAnimation(this, R.anim.hide_button_anims_up);
    }
    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "All ads removed! Please restart app for taking effect", Toast.LENGTH_LONG).show();
        AppConstant.setAdsFreeVersion(getApplicationContext(),true);
        Log.e("check sharepreferent ",String.valueOf(AppConstant.isRemoveAds(getApplicationContext())));
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, "Your Purchase has been canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void getIntentExtra() {
        Intent intent = getIntent();
        this.imageuri = intent.getData();
        this.tool_array = intent.getStringArrayExtra("tool_title");
        this.MaxResolution = intent.getIntExtra("picresolution", this.screenwidth);
    }

    private void getAspectRatio(String s, float f) {
        float f3;
        float f2;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(s, options);
        float f1 = ((float) options.outWidth) / ((float) options.outHeight);
        if (f1 > TextTrackStyle.DEFAULT_FONT_SCALE) {
            f3 = f;
            f2 = f3 / f1;
        } else {
            f2 = f;
            f3 = f2 * f1;
        }
        this.imagewidth = (int) f3;
        this.imageheight = (int) f2;
    }

    public Bitmap getResizedOriginalBitmap(String s, float f2) {
        try {
            Bitmap bitmap;
            Matrix matrix;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(s), null, options);
            int i = options.outWidth;
            int j = options.outHeight;
            int k = this.imagewidth;
            int l = this.imageheight;
            int i1 = 1;
            while (true) {
                if (i / 2 <= k) {
                    float f = ((float) k) / ((float) i);
                    float f1 = ((float) l) / ((float) j);
                    options.inJustDecodeBounds = false;
                    options.inDither = false;
                    options.inSampleSize = i1;
                    options.inScaled = false;
                    options.inPreferredConfig = Config.ARGB_8888;
                    try {
                        bitmap = BitmapFactory.decodeStream(new FileInputStream(s), null, options);
                        matrix = new Matrix();
                        matrix.postScale(f, f1);
                        matrix.postRotate(f2);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    i /= 2;
                    j /= 2;
                    i1 *= 2;
                }
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (FileNotFoundException e2) {
            return null;
        }
    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        }
        cursor.moveToFirst();
        String s = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return s;
    }

    private float getImageOrientation(String s) {
        try {
            int i = new ExifInterface(s).getAttributeInt("Orientation", 1);
            if (i == 6) {
                return 90.0f;
            }
            if (i == 3) {
                return BitmapDescriptorFactory.HUE_CYAN;
            }
            if (i == 8) {
                return BitmapDescriptorFactory.HUE_VIOLET;
            }
            return 0.0f;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return 0.0f;
        }
    }

    private void stringMatching() {
        for (String s : this.tool_array) {
            View view = getLayoutInflater().inflate(R.layout.pic_btn_layout, null);
            ImageButton imagebutton = (ImageButton) view.findViewById(R.id.btn_image);
            TextView textview = (TextView) view.findViewById(R.id.btn_txt);
            imagebutton.setOnClickListener(this);
            textview.setOnClickListener(this);
            if ("CROP".equalsIgnoreCase(s)) {
                textview.setText(getString(R.string.edt_crop));
                imagebutton.setImageResource(R.drawable.pic_crop);
                imagebutton.setId(2);
                textview.setId(2);
                setIcon_Crop();
            }
            this.pic_btn_gallery.addView(view);
        }
    }

    private void setIcon_Crop() {
        this.effapplied = Boolean.valueOf(false);
        this.pic_imageview.setVisibility(View.GONE);
        this.pic_cropImageView.setImageBitmap(pic_result);
        this.pic_cropImageView.setVisibility(View.VISIBLE);
        Animationview(this.pic_btn_gallery, this.crop_gallery);
        AnimationviewTop(this.pic_apply_layout, this.pic_donelayout, 2);
        String[] as = new String[]{"Custom", "1:1", "2:1", "1:2", "3:2", "2:3", "4:3", "4:6", "4:5", "5:6", "5:7", "9:16", "16:9"};
        for (int i = 0; i < as.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.pic_crop_layout, null);
            final Button btn_crop = (Button) view.findViewById(R.id.crop_btn);
            btn_crop.setId(i);
            btn_crop.setLayoutParams(new LayoutParams(-2, -2));
            btn_crop.setText(as[i]);
            this.crop_gallery.addView(view);
            btn_crop.setOnClickListener(new OnClickListener() {
                public void onClick(View view1) {
                    ImageRemake.this.effapplied = Boolean.valueOf(false);
                    switch (btn_crop.getId()) {
                        case 0:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(false);
                            return;
                        case 1:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(1, 1);
                            return;
                        case 2:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(2, 1);
                            return;
                        case 3:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(1, 2);
                            return;
                        case 4:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(3, 2);
                            return;
                        case 5:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(2, 3);
                            return;
                        case 6:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(4, 3);
                            return;
                        case 7:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(4, 6);
                            return;
                        case 8:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(4, 5);
                            return;
                        case 9:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(5, 6);
                            return;
                        case 10:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(5, 7);
                            return;
                        case 11:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(8, 10);
                            return;
                        case 12:
                            ImageRemake.this.pic_cropImageView.setFixedAspectRatio(true);
                            ImageRemake.this.pic_cropImageView.setAspectRatio(16, 9);
                            return;
                        default:
                            return;
                    }
                }
            });
        }
    }

    private void setIcon_Effects() {
        for (int i = 0; i < this.as.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.pic_effect_layout, null);
            final ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView textview = (TextView) view.findViewById(R.id.txt_view);
            imageView.setId(i);
            imageView.setLayoutParams(new LayoutParams(-2, -2));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), this.effectImages[i].intValue());
            textview.setText(this.as[i]);
            imageView.setImageBitmap(bitmap);
            this.effect_gallery.addView(view);
            imageView.setOnClickListener(new OnClickListener() {
                public void onClick(View view1) {
                    if (ImageRemake.this.sketchDone) {
                        new SketchAsnyTask().execute(new Integer[]{Integer.valueOf(imageView.getId())});
                        return;
                    }
                    return;
                }
            });
        }
    }

    public void onAnimationEnd(Animation arg0) {
    }

    public void onAnimationRepeat(Animation animation) {
    }

    public void onAnimationStart(Animation animation) {
    }

    public void onBackPressed() {
        ExitDidalog(getString(R.string.pic_exit_txt));
    }

    public void Animationview(final View hideanimview, final View showanimview) {
        hideanimview.startAnimation(this.animhidebtn);
        this.animhidebtn.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                hideanimview.setVisibility(View.GONE);
                showanimview.startAnimation(ImageRemake.this.animsgallerybtn);
                showanimview.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                hideanimview.setVisibility(View.VISIBLE);
            }
        });
    }

    public void AnimationviewTop(final View showanimview, final View hideanimview, final int Btnid) {
        hideanimview.startAnimation(this.animshowbtnup);
        this.txt_editor.startAnimation(this.animshowbtnup);
        this.animshowbtnup.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                ImageRemake.this.txt_editor.startAnimation(ImageRemake.this.animshowbtndown);
                hideanimview.setVisibility(View.GONE);
                showanimview.setVisibility(View.VISIBLE);
                showanimview.startAnimation(ImageRemake.this.animshowbtndown);
                switch (Btnid) {
                    case 1:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_effect));
                        return;
                    case 2:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_crop));
                        return;
                    case 3:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_vintage));
                        return;
                    case 4:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_frame));
                        return;
                    case 5:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_overlay));
                        return;
                    case 6:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_reset));
                        return;
                    case 7:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_border));
                        return;
                    case 8:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_orientation));
                        return;
                    case 9:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_editor));
                        return;
                    default:
                        ImageRemake.this.txt_editor.setText(ImageRemake.this.getString(R.string.edt_editor));
                        return;
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
    }

    public void ExitDidalog(String s) {
        this.exit_dialog = new Dialog(this);
        this.exit_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.exit_dialog.setContentView(R.layout.pic_reset_dialog);
        TextView textview1 = (TextView) this.exit_dialog.findViewById(R.id.pic_dialog_no);
        ((TextView) this.exit_dialog.findViewById(R.id.pic_dialog_yes)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageRemake.this.exit_dialog.dismiss();
                ImageRemake.this.setResult(0, new Intent());
            }
        });
        textview1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ImageRemake.this.exit_dialog.dismiss();
                ImageRemake.this.finish();
            }
        });
        this.exit_dialog.show();
    }

    private void checkcropIV() {
        if (this.pic_cropImageView.getVisibility() == 0) {
            this.pic_cropImageView.setVisibility(View.GONE);
            this.pic_imageview.setVisibility(View.VISIBLE);
        }
        if (this.crop_gallery.getVisibility() == 0) {
            this.crop_gallery.setVisibility(View.GONE);
        }
        if (this.effect_gallery.getVisibility() == 0) {
            this.effect_gallery.setVisibility(View.GONE);
        }
    }

    public void PicMakerDidalog(String s) {
        final Dialog picmaker_dialog = new Dialog(this);
        picmaker_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        picmaker_dialog.setContentView(R.layout.pic_reset_dialog);
        TextView textview = (TextView) picmaker_dialog.findViewById(R.id.pic_dialog_yes);
        TextView textview1 = (TextView) picmaker_dialog.findViewById(R.id.pic_dialog_no);
        textview.setText(getString(R.string.reset_edt));
        textview1.setText(getString(R.string.continue_edt));
        textview.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Bitmap bitmap = ImageRemake.pic_result;
                if (ImageRemake.this.Imagepath != null) {
                    picmaker_dialog.dismiss();
                    ImageRemake.this.Orientation = Float.valueOf(ImageRemake.this.getImageOrientation(ImageRemake.this.Imagepath));
                    ImageRemake.this.getAspectRatio(ImageRemake.this.Imagepath, (float) ImageRemake.this.MaxResolution);
                    ImageRemake.pic_result = ImageRemake.this.getResizedOriginalBitmap(ImageRemake.this.Imagepath, ImageRemake.this.Orientation.floatValue());
                    ImageRemake.this.pic_imageview.setImageBitmap(ImageRemake.pic_result);
                    Toast.makeText(ImageRemake.this.getApplicationContext(), "Your original image is back !!!", 0).show();
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                        System.gc();
                        return;
                    }
                    return;
                }
                Toast.makeText(ImageRemake.this.getApplicationContext(), "Invalid image path.", 0).show();
            }
        });
        textview1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                picmaker_dialog.dismiss();
            }
        });
        picmaker_dialog.show();
    }

    public void onClick(View view) {
        int i = view.getId();
        if (i == 2 && this.effect_gallery.getVisibility() == 0) {
            this.effect_gallery.setVisibility(View.GONE);
        }
        if (i == 8) {
            this.effapplied = Boolean.valueOf(false);
            checkcropIV();
            AnimationviewTop(this.pic_apply_layout, this.pic_donelayout, 8);
        }
        if (i == 1) {
            this.effapplied = Boolean.valueOf(false);
            checkcropIV();
            overlayid = -1;
            Animationview(this.pic_btn_gallery, this.effect_gallery);
            AnimationviewTop(this.pic_apply_layout, this.pic_donelayout, 1);
        }
        if (i == 3) {
            checkcropIV();
            overlayid = -1;
            AnimationviewTop(this.pic_apply_layout, this.pic_donelayout, 3);
        }
        if (i == 4) {
            this.effapplied = Boolean.valueOf(false);
            checkcropIV();
            overlayid = -1;
            AnimationviewTop(this.pic_apply_layout, this.pic_donelayout, 4);
        }
        if (i == 5) {
            checkcropIV();
            overlayid = -1;
            AnimationviewTop(this.pic_apply_layout, this.pic_donelayout, 5);
            return;
        }
        if (i == 7) {
            this.effapplied = Boolean.valueOf(false);
            checkcropIV();
        }
        if (i == 6) {
            this.effapplied = Boolean.valueOf(false);
            checkcropIV();
            PicMakerDidalog("You are loosing your edited image.Do you want to reset?");
        }
        if (i == R.id.pic_apply_layout) {
            appConstant.showFAd();
            Bitmap bitmap;
            if (this.crop_gallery.getVisibility() == 0) {
                bitmap = this.pic_cropImageView.getCroppedImage();
                pic_result = bitmap.copy(Config.ARGB_8888, true);
                pic_forSketch = bitmap.copy(Config.ARGB_8888, true);
                pic_forDraw = bitmap.copy(Config.ARGB_8888, true);
                this.crop_gallery.setVisibility(View.GONE);
                this.pic_cropImageView.setVisibility(View.GONE);
                this.pic_cropImageView.setImageResource(0);
                this.pic_imageview.setVisibility(View.VISIBLE);
                this.pic_imageview.setImageBitmap(pic_result);
                Toast.makeText(this, "Cropped", 0).show();
                new SketchAsnyTaskFirst().execute(new Integer[]{Integer.valueOf(6)});
            }
            if (this.effect_gallery.getVisibility() == 0) {
                this.effapplied.booleanValue();
                this.effect_gallery.setVisibility(View.GONE);
                this.seek_layout.setVisibility(View.GONE);
            }
            AnimationviewTop(this.pic_donelayout, this.pic_apply_layout, 9);
            this.pic_btn_gallery.startAnimation(this.anim_bottom_show);
            this.pic_btn_gallery.setVisibility(View.VISIBLE);
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_eff_image);
            setIcon_Effects();
            if (!(bitmap == null || bitmap.isRecycled())) {
                bitmap.recycle();
                System.gc();
            }
            this.effapplied = Boolean.valueOf(false);
            checkcropIV();
            overlayid = -1;
            Animationview(this.pic_btn_gallery, this.effect_gallery);
            AnimationviewTop(this.pic_donelayout, this.pic_apply_layout, 1);
        }
        if (i == R.id.pic_done_layout) {
            this.effapplied = Boolean.valueOf(false);
            try {
                saveBitmap(UUID.randomUUID().toString(), 100, pic_result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file = new File(this.sendimagepath);
            if (file.exists()) {
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(this, PhotoShare_Activity.class);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (bp != null) {
            bp.release();
        }
        appConstant.destroy();
        this.effapplied = Boolean.valueOf(false);
        if (!(GPUImageFilterTools.overlayBmp == null || GPUImageFilterTools.overlayBmp.isRecycled())) {
            GPUImageFilterTools.overlayBmp.recycle();
            GPUImageFilterTools.overlayBmp = null;
            System.gc();
        }
        if (!(pic_result == null || pic_result.isRecycled())) {
            pic_result.recycle();
            pic_result = null;
            System.gc();
        }
        for (int j = 0; j < this.as.length; j++) {
        }
        this.animhidebtn.cancel();
        this.animsgallerybtn.cancel();
        this.animshowbtnup.cancel();
        this.animshowbtndown.cancel();
        this.anim_bottom_show.cancel();
        this.anim_btnapply.cancel();
        unbindDrawables(findViewById(R.id.mainlayout));
    }

    private void unbindDrawables(View view) {
        try {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                ((ViewGroup) view).removeAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean saveBitmap(String s, int i, Bitmap bitmap) throws IOException {
        String s1 = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().toString())).append(File.separator).toString();
        new File(s1).mkdirs();
        new Options().inSampleSize = 5;
        this.sendimagepath = new StringBuilder(String.valueOf(s1)).append(File.separator).append(s).append(".jpg").toString();
        File file = new File(this.sendimagepath);
        String[] as = null;
        if (file.exists()) {
            try {
                file.delete();
                file.createNewFile();
            } catch (NullPointerException e) {
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else {
            file.createNewFile();
        }
        BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(CompressFormat.PNG, i, bufferedoutputstream);
        bufferedoutputstream.flush();
        bufferedoutputstream.close();
        as = new String[]{file.toString()};
        MediaScannerConnection.scanFile(this, as, null, new OnScanCompletedListener() {
            public void onScanCompleted(String s1, Uri uri) {
            }
        });
        return true;
    }
}
