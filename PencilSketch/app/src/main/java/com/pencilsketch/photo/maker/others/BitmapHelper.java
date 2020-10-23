package com.pencilsketch.photo.maker.others;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

public abstract class BitmapHelper {
    protected Activity activity = null;
    Handler handler = null;
    protected int no = 2;

    public abstract Bitmap getSketchFromBH(Bitmap bitmap) throws Throwable;

    public BitmapHelper(Activity activity, Handler handler) {
        this.handler = handler;
        this.activity = activity;
    }

    public final void handlerFunc(int i, int i2, double d, String str) {
        int i3 = 1;
        if (this.handler != null) {
            if (i == 0) {
                Handler handler = this.handler;
                int i4 = (int) d;
                if (str == null) {
                    i3 = 0;
                }
                this.handler.sendMessage(Message.obtain(handler, i2, i4, i3));
            } else if (i == 1) {
                this.handler.sendEmptyMessage((int) d);
            } else if (i == 2) {
                this.handler.sendEmptyMessage(((int) d) + 100);
            }
        }
    }

    public final void runHandlerFunc(int i, int i2, double d) {
        handlerFunc(i, i2, d, null);
    }
}
