package com.pencil.sketch.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.google.android.gms.cast.TextTrackStyle;

public class SandboxView extends View implements OnTouchListener {
    private float angle = 0.0f;
    private final Bitmap bitmap;
    private final int height;
    private boolean isInitialized = false;
    private Vector2D position = new Vector2D();
    private float scale = TextTrackStyle.DEFAULT_FONT_SCALE;
    private TouchManager touchManager = new TouchManager(2);
    private Matrix transform = new Matrix();
    private Vector2D vca = null;
    private Vector2D vcb = null;
    private Vector2D vpa = null;
    private Vector2D vpb = null;
    private final int width;

    public SandboxView(Context context, Bitmap bitmap) {
        super(context);
        this.bitmap = bitmap;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        setOnTouchListener(this);
    }

    private static float getDegreesFromRadians(float angle) {
        return (float) ((((double) angle) * 180.0d) / 3.141592653589793d);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.isInitialized) {
            this.position.set((float) (getWidth() / 2), (float) (getHeight() / 2));
            this.isInitialized = true;
        }
        Paint paint = new Paint();
        this.transform.reset();
        this.transform.postTranslate(((float) (-this.width)) / 2.0f, ((float) (-this.height)) / 2.0f);
        this.transform.postRotate(getDegreesFromRadians(this.angle));
        this.transform.postScale(this.scale, this.scale);
        this.transform.postTranslate(this.position.getX(), this.position.getY());
        canvas.drawBitmap(this.bitmap, this.transform, paint);
    }

    public boolean onTouch(View v, MotionEvent event) {
        this.vca = null;
        this.vcb = null;
        this.vpa = null;
        this.vpb = null;
        try {
            this.touchManager.update(event);
            if (this.touchManager.getPressCount() == 1) {
                this.vca = this.touchManager.getPoint(0);
                this.vpa = this.touchManager.getPreviousPoint(0);
                this.position.add(this.touchManager.moveDelta(0));
            } else if (this.touchManager.getPressCount() == 2) {
                this.vca = this.touchManager.getPoint(0);
                this.vpa = this.touchManager.getPreviousPoint(0);
                this.vcb = this.touchManager.getPoint(1);
                this.vpb = this.touchManager.getPreviousPoint(1);
                Vector2D current = this.touchManager.getVector(0, 1);
                Vector2D previous = this.touchManager.getPreviousVector(0, 1);
                float currentDistance = current.getLength();
                float previousDistance = previous.getLength();
                if (previousDistance != 0.0f) {
                    this.scale *= currentDistance / previousDistance;
                }
                this.angle -= Vector2D.getSignedAngleBetween(current, previous);
            }
            invalidate();
        } catch (Throwable th) {
        }
        return true;
    }
}
