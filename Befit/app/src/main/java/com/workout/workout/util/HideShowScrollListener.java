package com.workout.workout.util;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

public abstract class HideShowScrollListener extends OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;
    private boolean controlsVisible = true;
    private int scrolledDistance = 0;

    public abstract void onHide();

    public abstract void onShow();

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (this.scrolledDistance > 20 && this.controlsVisible) {
            onHide();
            this.controlsVisible = false;
            this.scrolledDistance = 0;
        } else if (this.scrolledDistance < -20 && !this.controlsVisible) {
            onShow();
            this.controlsVisible = true;
            this.scrolledDistance = 0;
        }
        if ((this.controlsVisible && dy > 0) || (!this.controlsVisible && dy < 0)) {
            this.scrolledDistance += dy;
        }
    }
}
