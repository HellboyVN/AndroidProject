package com.pencilsketch.photo.maker.others;

import android.app.Activity;
import android.os.Handler;
import java.lang.ref.WeakReference;

public class WeakRefHandler extends Handler {
    protected WeakReference<Activity> weakRef;

    public WeakRefHandler(Activity activity) {
        this.weakRef = new WeakReference(activity);
    }
}
