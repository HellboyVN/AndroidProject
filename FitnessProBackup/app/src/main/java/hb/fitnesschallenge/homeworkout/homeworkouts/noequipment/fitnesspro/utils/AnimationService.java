package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

public class AnimationService {
    private static AnimationService INSTANCE = null;
    final float DEFAULT_LENGTH = 100.0f;
    final float DEFAULT_SPEED = 1000.0f;
    private final Context context;

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AnimationService(context);
        }
    }

    public static AnimationService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        throw new IllegalStateException("service not initialized");
    }

    private AnimationService(Context context) {
        this.context = context;
    }

    private int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public void goToLeft(Context context, View view, AnimatorListenerAdapter listener) {
        if (context != null && view != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", new float[]{(float) ((getScreenWidth(context) - view.getWidth()) / 2), (float) (-view.getWidth())});
            animator.setInterpolator(new LinearInterpolator());
            float delta = Math.abs(370) / 100.0f;
            if (delta > 1.0f) {
                animator.setDuration((long) ((int) (1000.0f / delta)));
            } else {
                animator.setDuration((long) ((int) ((delta + 1.0f) * 1000.0f)));
            }
            animator.addListener(listener);
            animator.setRepeatCount(0);
            animator.start();
        }
    }

    public void goToCenter(Context context, View view, AnimatorListenerAdapter listener) {
        if (context != null && view != null) {
            int width = getScreenWidth(context);
            ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", new float[]{(float) width, (float) ((width - view.getWidth()) / 2)});
            animator.setInterpolator(new LinearInterpolator());
            float delta = Math.abs(370) / 100.0f;
            if (delta > 1.0f) {
                animator.setDuration((long) ((int) (1000.0f / delta)));
            } else {
                animator.setDuration((long) ((int) ((delta + 1.0f) * 1000.0f)));
            }
            animator.addListener(listener);
            animator.setRepeatCount(0);
            animator.start();
        }
    }

    public Animation getLeft() {
        return AnimationUtils.loadAnimation(this.context, R.anim.left);
    }

    public Animation getRight() {
        return AnimationUtils.loadAnimation(this.context, R.anim.right);
    }

    public Animation getGoRight() {
        return AnimationUtils.loadAnimation(this.context, R.anim.go_right);
    }

    public Animation getGoLeft() {
        return AnimationUtils.loadAnimation(this.context, R.anim.go_left);
    }
}
