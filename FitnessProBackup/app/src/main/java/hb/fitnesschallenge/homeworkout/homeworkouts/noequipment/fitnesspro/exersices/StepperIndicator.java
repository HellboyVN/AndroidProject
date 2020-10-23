package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import java.util.ArrayList;
import java.util.List;

public class StepperIndicator extends View implements OnPageChangeListener {
    private static final int DEFAULT_ANIMATION_DURATION = 250;
    private static final float EXPAND_MARK = 1.3f;
    private static final int STEP_COUNT_INVALID = -1;
    private float animCheckRadius;
    private int animDuration;
    private float animIndicatorRadius;
    private float animProgress;
    private AnimatorSet animatorSet;
    private ObjectAnimator checkAnimator;
    private float checkRadius;
    private Paint circlePaint;
    private float circleRadius;
    private int currentStep;
    private Bitmap doneIcon;
    private GestureDetector gestureDetector;
    private OnGestureListener gestureListener;
    private ObjectAnimator indicatorAnimator;
    private Paint indicatorPaint;
    private float indicatorRadius;
    private float[] indicators;
    private ObjectAnimator lineAnimator;
    private Paint lineDoneAnimatedPaint;
    private Paint lineDonePaint;
    private float lineLength;
    private float lineMargin;
    private Paint linePaint;
    private List<Path> linePathList;
    private final List<OnStepClickListener> onStepClickListeners;
    private ViewPager pager;
    private int previousStep;
    private boolean showDoneIcon;
    private int stepCount;
    private List<RectF> stepsClickAreas;

    public interface OnStepClickListener {
        void onStepClicked(int i);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private int currentStep;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentStep = in.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.currentStep);
        }
    }

    public StepperIndicator(Context context) {
        this(context, null);
    }

    public StepperIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepperIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.linePathList = new ArrayList();
        this.onStepClickListeners = new ArrayList(0);
        this.gestureListener = new SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (StepperIndicator.this.onStepClickListeners.isEmpty()) {
                    return super.onSingleTapConfirmed(e);
                }
                float xCord = e.getX();
                float yCord = e.getY();
                Log.d("StepperIndicator", "onSingleTapConfirmed: clicked = [" + xCord + ", " + yCord + "]");
                int clickedStep = -1;
                for (int i = 0; i < StepperIndicator.this.stepsClickAreas.size(); i++) {
                    if (((RectF) StepperIndicator.this.stepsClickAreas.get(i)).contains(xCord, yCord)) {
                        Log.d("StepperIndicator", "onSingleTapConfirmed: step #" + i + " clicked!");
                        clickedStep = i;
                        break;
                    }
                }
                if (clickedStep != -1) {
                    for (OnStepClickListener listener : StepperIndicator.this.onStepClickListeners) {
                        listener.onStepClicked(clickedStep);
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        };
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public StepperIndicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.linePathList = new ArrayList();
        this.onStepClickListeners = new ArrayList(0);
        this.gestureListener =new SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (StepperIndicator.this.onStepClickListeners.isEmpty()) {
                    return super.onSingleTapConfirmed(e);
                }
                float xCord = e.getX();
                float yCord = e.getY();
                Log.d("StepperIndicator", "onSingleTapConfirmed: clicked = [" + xCord + ", " + yCord + "]");
                int clickedStep = -1;
                for (int i = 0; i < StepperIndicator.this.stepsClickAreas.size(); i++) {
                    if (((RectF) StepperIndicator.this.stepsClickAreas.get(i)).contains(xCord, yCord)) {
                        Log.d("StepperIndicator", "onSingleTapConfirmed: step #" + i + " clicked!");
                        clickedStep = i;
                        break;
                    }
                }
                if (clickedStep != -1) {
                    for (OnStepClickListener listener : StepperIndicator.this.onStepClickListeners) {
                        listener.onStepClicked(clickedStep);
                    }
                }
                return super.onSingleTapConfirmed(e);
            }
        };
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        Resources resources = getResources();
        int defaultCircleColor = ContextCompat.getColor(context, R.color.stpi_default_circle_color);
        float defaultCircleRadius = resources.getDimension(R.dimen.stpi_default_circle_radius);
        float defaultCircleStrokeWidth = resources.getDimension(R.dimen.stpi_default_circle_stroke_width);
        int defaultPrimaryColor = getPrimaryColor(context);
        int defaultIndicatorColor = defaultPrimaryColor;
        float defaultIndicatorRadius = resources.getDimension(R.dimen.stpi_default_indicator_radius);
        float defaultLineStrokeWidth = resources.getDimension(R.dimen.stpi_default_line_stroke_width);
        float defaultLineMargin = resources.getDimension(R.dimen.stpi_default_line_margin);
        int defaultLineColor = ContextCompat.getColor(context, R.color.stpi_default_line_color);
        int defaultLineDoneColor = defaultPrimaryColor;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepperIndicator, defStyleAttr, 0);
        this.circlePaint = new Paint();
        this.circlePaint.setStrokeWidth(a.getDimension(3, defaultCircleStrokeWidth));
        this.circlePaint.setStyle(Style.STROKE);
        this.circlePaint.setColor(a.getColor(1, defaultCircleColor));
        this.circlePaint.setAntiAlias(true);
        this.indicatorPaint = new Paint(this.circlePaint);
        this.indicatorPaint.setStyle(Style.FILL);
        this.indicatorPaint.setColor(a.getColor(4, defaultIndicatorColor));
        this.indicatorPaint.setAntiAlias(true);
        this.linePaint = new Paint();
        this.linePaint.setStrokeWidth(a.getDimension(9, defaultLineStrokeWidth));
        this.linePaint.setStrokeCap(Cap.ROUND);
        this.linePaint.setStyle(Style.STROKE);
        this.linePaint.setColor(a.getColor(6, defaultLineColor));
        this.linePaint.setAntiAlias(true);
        this.lineDonePaint = new Paint(this.linePaint);
        this.lineDonePaint.setColor(a.getColor(7, defaultLineDoneColor));
        this.lineDoneAnimatedPaint = new Paint(this.lineDonePaint);
        this.circleRadius = a.getDimension(2, defaultCircleRadius);
        this.checkRadius = this.circleRadius + (this.circlePaint.getStrokeWidth() / 2.0f);
        this.indicatorRadius = a.getDimension(5, defaultIndicatorRadius);
        this.animIndicatorRadius = this.indicatorRadius;
        this.animCheckRadius = this.checkRadius;
        this.lineMargin = a.getDimension(8, defaultLineMargin);
        setStepCount(a.getInteger(11, 2));
        this.animDuration = a.getInteger(0, 250);
        this.showDoneIcon = a.getBoolean(10, true);
        a.recycle();
        if (this.showDoneIcon) {
            this.doneIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_done_white_18dp);
        }
        if (isInEditMode()) {
            this.currentStep = Math.max((int) Math.ceil((double) (((float) this.stepCount) / 2.0f)), 1);
        }
        this.gestureDetector = new GestureDetector(getContext(), this.gestureListener);
    }

    public static int getPrimaryColor(Context context) {
        int color = context.getResources().getIdentifier("colorPrimary", "attr", context.getPackageName());
        if (color != 0) {
            TypedValue t = new TypedValue();
            context.getTheme().resolveAttribute(color, t, true);
            return t.data;
        } else if (VERSION.SDK_INT >= 21) {
            TypedArray t = context.obtainStyledAttributes(new int[]{16843827});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.stpi_default_primary_color));
            t.recycle();
            return color;
        } else {
            TypedArray t = context.obtainStyledAttributes(new int[]{R.attr.colorPrimary});
            color = t.getColor(0, ContextCompat.getColor(context, R.color.stpi_default_primary_color));
            t.recycle();
            return color;
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        compute();
    }

    private void compute() {
        int i;
        this.indicators = new float[this.stepCount];
        this.linePathList.clear();
        float startX = (this.circleRadius * EXPAND_MARK) + (this.circlePaint.getStrokeWidth() / 2.0f);
        float divider = (((float) getMeasuredWidth()) - (startX * 2.0f)) / ((float) (this.stepCount - 1));
        this.lineLength = (divider - ((this.circleRadius * 2.0f) + this.circlePaint.getStrokeWidth())) - (this.lineMargin * 2.0f);
        for (i = 0; i < this.indicators.length; i++) {
            this.indicators[i] = (((float) i) * divider) + startX;
        }
        for (i = 0; i < this.indicators.length - 1; i++) {
            float position = ((this.indicators[i] + this.indicators[i + 1]) / 2.0f) - (this.lineLength / 2.0f);
            Path linePath = new Path();
            linePath.moveTo(position, (float) (getMeasuredHeight() / 2));
            linePath.lineTo(this.lineLength + position, (float) (getMeasuredHeight() / 2));
            this.linePathList.add(linePath);
        }
        computeStepsClickAreas();
    }

    private void computeStepsClickAreas() {
        if (this.stepCount == -1) {
            throw new IllegalArgumentException("stepCount wasn't setup yet. Make sure you call setStepCount() before computing the steps click area!");
        } else if (this.indicators == null) {
            throw new IllegalArgumentException("indicators wasn't setup yet. Make sure the indicators are initialized and setup correctly before trying to compute the click area for each step!");
        } else {
            this.stepsClickAreas = new ArrayList(this.stepCount);
            for (float indicator : this.indicators) {
                this.stepsClickAreas.add(new RectF(indicator - this.circleRadius, (float) getTop(), indicator + this.circleRadius, (float) getBottom()));
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        float centerY = ((float) getMeasuredHeight()) / 2.0f;
        boolean inAnimation = false;
        boolean inLineAnimation = false;
        boolean inIndicatorAnimation = false;
        boolean inCheckAnimation = false;
        if (VERSION.SDK_INT >= 11) {
            inAnimation = this.animatorSet != null && this.animatorSet.isRunning();
            inLineAnimation = this.lineAnimator != null && this.lineAnimator.isRunning();
            inIndicatorAnimation = this.indicatorAnimator != null && this.indicatorAnimator.isRunning();
            inCheckAnimation = this.checkAnimator != null && this.checkAnimator.isRunning();
        }
        boolean drawToNext = this.previousStep == this.currentStep + -1;
        boolean drawFromNext = this.previousStep == this.currentStep + 1;
        int i = 0;
        while (i < this.indicators.length) {
            float indicator = this.indicators[i];
            boolean drawCheck = i < this.currentStep || (drawFromNext && i == this.currentStep);
            canvas.drawCircle(indicator, centerY, this.circleRadius, this.circlePaint);
            if ((i == this.currentStep && !drawFromNext) || (i == this.previousStep && drawFromNext && inAnimation)) {
                canvas.drawCircle(indicator, centerY, this.animIndicatorRadius, this.indicatorPaint);
            }
            if (drawCheck) {
                float radius = this.checkRadius;
                if ((i == this.previousStep && drawToNext) || (i == this.currentStep && drawFromNext)) {
                    radius = this.animCheckRadius;
                }
                canvas.drawCircle(indicator, centerY, radius, this.indicatorPaint);
                if (!isInEditMode() && this.showDoneIcon && (!(i == this.previousStep || i == this.currentStep) || (!inCheckAnimation && (i != this.currentStep || inAnimation)))) {
                    canvas.drawBitmap(this.doneIcon, indicator - ((float) (this.doneIcon.getWidth() / 2)), centerY - ((float) (this.doneIcon.getHeight() / 2)), null);
                }
            }
            if (i < this.linePathList.size()) {
                if (i >= this.currentStep) {
                    canvas.drawPath((Path) this.linePathList.get(i), this.linePaint);
                    if (i == this.currentStep && drawFromNext && (inLineAnimation || inIndicatorAnimation)) {
                        canvas.drawPath((Path) this.linePathList.get(i), this.lineDoneAnimatedPaint);
                    }
                } else if (i == this.currentStep - 1 && drawToNext && inLineAnimation) {
                    canvas.drawPath((Path) this.linePathList.get(i), this.linePaint);
                    canvas.drawPath((Path) this.linePathList.get(i), this.lineDoneAnimatedPaint);
                } else {
                    canvas.drawPath((Path) this.linePathList.get(i), this.lineDonePaint);
                }
            }
            i++;
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int desiredHeight = (int) Math.ceil((double) (((this.circleRadius * EXPAND_MARK) * 2.0f) + this.circlePaint.getStrokeWidth()));
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = widthMode == 1073741824 ? widthSize : getSuggestedMinimumWidth();
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setStepCount(int stepCount) {
        if (stepCount < 2) {
            throw new IllegalArgumentException("stepCount must be >= 2");
        }
        this.stepCount = stepCount;
        this.currentStep = 0;
        compute();
        invalidate();
    }

    public int getStepCount() {
        return this.stepCount;
    }

    public int getCurrentStep() {
        return this.currentStep;
    }

    @UiThread
    public void setCurrentStep(int currentStep) {
        if (currentStep < 0 || currentStep > this.stepCount) {
            throw new IllegalArgumentException("Invalid step value " + currentStep);
        }
        this.previousStep = this.currentStep;
        this.currentStep = currentStep;
        if (VERSION.SDK_INT >= 11) {
            if (this.animatorSet != null) {
                this.animatorSet.cancel();
            }
            this.animatorSet = null;
            this.lineAnimator = null;
            this.indicatorAnimator = null;
            if (currentStep == this.previousStep + 1) {
                this.animatorSet = new AnimatorSet();
                this.lineAnimator = ObjectAnimator.ofFloat(this, "animProgress", new float[]{1.0f, 0.0f});
                this.checkAnimator = ObjectAnimator.ofFloat(this, "animCheckRadius", new float[]{this.indicatorRadius, this.checkRadius * EXPAND_MARK, this.checkRadius});
                this.animIndicatorRadius = 0.0f;
                this.indicatorAnimator = ObjectAnimator.ofFloat(this, "animIndicatorRadius", new float[]{0.0f, this.indicatorRadius * 1.4f, this.indicatorRadius});
                this.animatorSet.play(this.lineAnimator).with(this.checkAnimator).before(this.indicatorAnimator);
            } else if (currentStep == this.previousStep - 1) {
                this.animatorSet = new AnimatorSet();
                this.indicatorAnimator = ObjectAnimator.ofFloat(this, "animIndicatorRadius", new float[]{this.indicatorRadius, 0.0f});
                this.animProgress = 1.0f;
                this.lineDoneAnimatedPaint.setPathEffect(null);
                this.lineAnimator = ObjectAnimator.ofFloat(this, "animProgress", new float[]{0.0f, 1.0f});
                this.animCheckRadius = this.checkRadius;
                this.checkAnimator = ObjectAnimator.ofFloat(this, "animCheckRadius", new float[]{this.checkRadius, this.indicatorRadius});
                this.animatorSet.playSequentially(new Animator[]{this.indicatorAnimator, this.lineAnimator, this.checkAnimator});
            }
            if (this.animatorSet != null) {
                this.lineAnimator.setDuration((long) Math.min(500, this.animDuration));
                this.lineAnimator.setInterpolator(new DecelerateInterpolator());
                this.indicatorAnimator.setDuration(this.lineAnimator.getDuration() / 2);
                this.checkAnimator.setDuration(this.lineAnimator.getDuration() / 2);
                this.animatorSet.start();
            }
        }
        invalidate();
    }

    public void setAnimProgress(float animProgress) {
        this.animProgress = animProgress;
        this.lineDoneAnimatedPaint.setPathEffect(createPathEffect(this.lineLength, animProgress, 0.0f));
        invalidate();
    }

    public void setAnimIndicatorRadius(float animIndicatorRadius) {
        this.animIndicatorRadius = animIndicatorRadius;
        invalidate();
    }

    public void setAnimCheckRadius(float animCheckRadius) {
        this.animCheckRadius = animCheckRadius;
        invalidate();
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset) {
        return new DashPathEffect(new float[]{pathLength, pathLength}, Math.max(phase * pathLength, offset));
    }

    public void setViewPager(ViewPager pager) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        setViewPager(pager, pager.getAdapter().getCount());
    }

    public void setViewPager(ViewPager pager, boolean keepLastPage) {
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        setViewPager(pager, pager.getAdapter().getCount() - (keepLastPage ? 1 : 0));
    }

    public void setViewPager(ViewPager pager, int stepCount) {
        if (this.pager != pager) {
            if (this.pager != null) {
                pager.removeOnPageChangeListener(this);
            }
            if (pager.getAdapter() == null) {
                throw new IllegalStateException("ViewPager does not have adapter instance.");
            }
            this.pager = pager;
            this.stepCount = stepCount;
            this.currentStep = 0;
            pager.addOnPageChangeListener(this);
            invalidate();
        }
    }

    public void addOnStepClickListener(OnStepClickListener listener) {
        this.onStepClickListeners.add(listener);
    }

    public void removeOnStepClickListener(OnStepClickListener listener) {
        this.onStepClickListeners.remove(listener);
    }

    public void clearOnStepClickListeners() {
        this.onStepClickListeners.clear();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    public void onPageSelected(int position) {
        setCurrentStep(position);
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.currentStep = savedState.currentStep;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.currentStep = this.currentStep;
        return savedState;
    }
}
