package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;

public class AnimationService {
    private static AnimationService INSTANCE = null;
    final float DEFAULT_LENGTH = 100.0f;
    final float DEFAULT_SPEED = 1000.0f;

    public static AnimationService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AnimationService();
        }
        return INSTANCE;
    }

    private AnimationService() {
    }

    private int getScreenWidth(Context context) {
        return ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getWidth();
    }

    public void goToLeft(View view, AnimatorListenerAdapter listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", new float[]{(float) ((getScreenWidth(view.getContext()) - view.getWidth()) / 2), (float) (-view.getWidth())});
        animator.setInterpolator(new LinearInterpolator());
        float delta = Math.abs(Constants.MAX_WEIGHT - Constants.MIN_WEIGHT) / 100.0f;
        if (delta > 1.0f) {
            animator.setDuration((long) ((int) (1000.0f / delta)));
        } else {
            animator.setDuration((long) ((int) ((delta + 1.0f) * 1000.0f)));
        }
        animator.addListener(listener);
        animator.setRepeatCount(0);
        animator.start();
    }

    public void goToCenter(View view, AnimatorListenerAdapter listener) {
        int width = getScreenWidth(view.getContext());
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "x", new float[]{(float) width, (float) ((width - view.getWidth()) / 2)});
        animator.setInterpolator(new LinearInterpolator());
        float delta = Math.abs(Constants.MAX_WEIGHT - Constants.MIN_WEIGHT) / 100.0f;
        if (delta > 1.0f) {
            animator.setDuration((long) ((int) (1000.0f / delta)));
        } else {
            animator.setDuration((long) ((int) ((delta + 1.0f) * 1000.0f)));
        }
        animator.addListener(listener);
        animator.setRepeatCount(0);
        animator.start();
    }

    public Animation getLeft(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.left);
    }

    public Animation getRight(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.right);
    }

    public Animation getGoRight(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.go_right);
    }

    public Animation getGoLeft(Context context) {
        return AnimationUtils.loadAnimation(context, R.anim.go_left);
    }
}
