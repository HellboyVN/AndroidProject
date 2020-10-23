package com.pencilsketch.photo.maker.others;

import android.app.Activity;
import com.pencil.sketch.photo.ImageRemake;

public final class ActivityHandler extends WeakRefHandler {
    final ImageRemake imageRemake;

    public ActivityHandler(ImageRemake sketchEdit, Activity activity) {
        super(activity);
        this.imageRemake = sketchEdit;
    }
}
