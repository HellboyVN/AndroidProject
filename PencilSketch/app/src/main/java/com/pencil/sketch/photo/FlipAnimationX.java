package com.pencil.sketch.photo;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class FlipAnimationX extends Animation {
    private Camera camera;
    private float centerX;
    private float centerY;
    private View fromView;
    private View toView;

    public FlipAnimationX(View view, View view1) {
        this.fromView = view;
        this.toView = view1;
        setDuration(300);
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
    }

    protected void applyTransformation(float f, Transformation transformation) {
        float f1 = (float) ((180.0d * (((double) f) * 3.141592653589793d)) / 3.141592653589793d);
        if (f >= 0.5f) {
            f1 -= BitmapDescriptorFactory.HUE_CYAN;
            this.fromView.setVisibility(View.VISIBLE);
            this.toView.setVisibility(View.VISIBLE);
        }
        Matrix matrix = transformation.getMatrix();
        this.camera.save();
        this.camera.rotateY(f1);
        this.camera.getMatrix(matrix);
        this.camera.restore();
        matrix.preTranslate(-this.centerX, -this.centerY);
        matrix.postTranslate(this.centerX, this.centerY);
    }

    public void initialize(int i, int j, int k, int l) {
        super.initialize(i, j, k, l);
        this.centerX = (float) (i / 2);
        this.centerY = (float) (j / 2);
        this.camera = new Camera();
    }
}
