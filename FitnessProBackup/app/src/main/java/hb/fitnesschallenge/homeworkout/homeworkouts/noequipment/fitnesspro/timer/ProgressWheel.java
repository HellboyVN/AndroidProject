package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.timer;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners.PlayPauseListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ResourceService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;

public class ProgressWheel extends View {
    private int barColor = -1442840576;
    private int barLength = 60;
    private Paint barPaint = new Paint();
    private int barWidth = 20;
    private RectF circleBounds = new RectF();
    private int circleColor = 0;
    private RectF circleInnerContour = new RectF();
    private RectF circleOuterContour = new RectF();
    private Paint circlePaint = new Paint();
    private int circleRadius = 80;
    private int contourColor = -1442840576;
    private Paint contourPaint = new Paint();
    private float contourSize = 0.0f;
    private int delayMillis = 0;
    private int fullRadius = 100;
    private boolean isPlaying = false;
    boolean isSpinning = false;
    private int layout_height = 0;
    private int layout_width = 0;
    private Bitmap mCurrentRunningState = this.mIcPlay;
    private Bitmap mIcPause = ResourceService.getBitmap(getContext(), R.drawable.ic_pause);
    private Bitmap mIcPlay = ResourceService.getBitmap(getContext(), R.drawable.ic_play);
    private PlayPauseListener mPlayPauseListener;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;
    private int paddingTop = 5;
    int progress = 0;
    private RectF rectBounds = new RectF();
    private int rimColor = -1428300323;
    private Paint rimPaint = new Paint();
    private int rimWidth = 20;
    private Paint runningStatePaint = new Paint();
    private Handler spinHandler = new Handler() {
        public void handleMessage(Message msg) {
            ProgressWheel.this.invalidate();
            if (ProgressWheel.this.isSpinning) {
                ProgressWheel progressWheel = ProgressWheel.this;
                progressWheel.progress += ProgressWheel.this.spinSpeed;
                if (ProgressWheel.this.progress > 360) {
                    ProgressWheel.this.progress = 0;
                }
                ProgressWheel.this.spinHandler.sendEmptyMessageDelayed(0, (long) ProgressWheel.this.delayMillis);
            }
        }
    };
    private int spinSpeed = 2;
    private String[] splitText = new String[0];
    private String[] splitTitle = new String[0];
    private String text = "";
    private int textColor = ViewCompat.MEASURED_STATE_MASK;
    private Paint textPaint = new Paint();
    private int textSize = 20;
    private String title = "";
    private Paint titlePaint = new Paint();

    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.ProgressWheel));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int widthWithoutPadding = (width - getPaddingLeft()) - getPaddingRight();
        int heigthWithoutPadding = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        if (widthWithoutPadding > heigthWithoutPadding) {
            size = heigthWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        setMeasuredDimension((getPaddingLeft() + size) + getPaddingRight(), (getPaddingTop() + size) + getPaddingBottom());
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.layout_width = w;
        this.layout_height = h;
        setupBounds();
        setupPaints();
        invalidate();
        setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProgressWheel.this.trigger();
            }
        });
    }

    public void trigger() {
        if (this.mPlayPauseListener != null) {
            if (this.isPlaying) {
                this.mPlayPauseListener.onTimerPause();
                this.mCurrentRunningState = this.mIcPlay;
            } else {
                this.mPlayPauseListener.onTimerPlay();
                this.mCurrentRunningState = this.mIcPause;
            }
            this.isPlaying = !this.isPlaying;
            invalidate();
        }
    }

    public void playTimer() {
        if (this.mPlayPauseListener != null) {
            this.mPlayPauseListener.onTimerPlay();
            this.mCurrentRunningState = this.mIcPause;
            this.isPlaying = true;
            invalidate();
        }
    }

    public void pauseTimer() {
        if (this.mPlayPauseListener != null) {
            this.mPlayPauseListener.onTimerPause();
            this.mCurrentRunningState = this.mIcPlay;
            this.isPlaying = false;
            invalidate();
        }
    }

    public void onFinish() {
        this.mCurrentRunningState = this.mIcPlay;
        invalidate();
    }

    private void setupPaints() {
        this.barPaint.setColor(this.barColor);
        this.barPaint.setAntiAlias(true);
        this.barPaint.setStyle(Style.STROKE);
        this.barPaint.setStrokeWidth((float) this.barWidth);
        this.rimPaint.setColor(this.rimColor);
        this.rimPaint.setAntiAlias(true);
        this.rimPaint.setStyle(Style.STROKE);
        this.rimPaint.setStrokeWidth((float) this.rimWidth);
        this.circlePaint.setColor(this.circleColor);
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setStyle(Style.FILL);
        this.textPaint.setColor(this.textColor);
        this.textPaint.setStyle(Style.FILL);
        this.textPaint.setAntiAlias(true);
        this.textPaint.setTypeface(TypeFaceService.getInstance().getDensRegular(getContext()));
        this.textPaint.setTextSize((float) this.textSize);
        this.titlePaint.setColor(this.textColor);
        this.titlePaint.setStyle(Style.FILL);
        this.titlePaint.setAntiAlias(true);
        this.titlePaint.setTypeface(TypeFaceService.getInstance().getRobotoLight(getContext()));
        this.titlePaint.setTextSize((float) ((int) TypedValue.applyDimension(1, 20.0f, getResources().getDisplayMetrics())));
        this.contourPaint.setColor(this.contourColor);
        this.contourPaint.setAntiAlias(true);
        this.contourPaint.setStyle(Style.STROKE);
        this.contourPaint.setStrokeWidth(this.contourSize);
        this.runningStatePaint.setAntiAlias(true);
    }

    private void setupBounds() {
        int minValue = Math.min(this.layout_width, this.layout_height);
        int xOffset = this.layout_width - minValue;
        int yOffset = this.layout_height - minValue;
        this.paddingTop = getPaddingTop() + (yOffset / 2);
        this.paddingBottom = getPaddingBottom() + (yOffset / 2);
        this.paddingLeft = getPaddingLeft() + (xOffset / 2);
        this.paddingRight = getPaddingRight() + (xOffset / 2);
        int width = getWidth();
        int height = getHeight();
        this.rectBounds = new RectF((float) this.paddingLeft, (float) this.paddingTop, (float) (width - this.paddingRight), (float) (height - this.paddingBottom));
        this.circleBounds = new RectF((float) (this.paddingLeft + this.barWidth), (float) (this.paddingTop + this.barWidth), (float) ((width - this.paddingRight) - this.barWidth), (float) ((height - this.paddingBottom) - this.barWidth));
        this.circleInnerContour = new RectF((this.circleBounds.left + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f), (this.circleBounds.top + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f), (this.circleBounds.right - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.bottom - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f));
        this.circleOuterContour = new RectF((this.circleBounds.left - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.top - (((float) this.rimWidth) / 2.0f)) - (this.contourSize / 2.0f), (this.circleBounds.right + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f), (this.circleBounds.bottom + (((float) this.rimWidth) / 2.0f)) + (this.contourSize / 2.0f));
        this.fullRadius = ((width - this.paddingRight) - this.barWidth) / 2;
        this.circleRadius = (this.fullRadius - this.barWidth) + 1;
    }

    private void parseAttributes(TypedArray a) {
        this.barWidth = (int) a.getDimension(2, (float) this.barWidth);
        this.rimWidth = (int) a.getDimension(9, (float) this.rimWidth);
        this.spinSpeed = (int) a.getDimension(10, (float) this.spinSpeed);
        this.delayMillis = a.getInteger(6, this.delayMillis);
        if (this.delayMillis < 0) {
            this.delayMillis = 0;
        }
        this.barColor = a.getColor(0, this.barColor);
        this.barLength = (int) a.getDimension(1, (float) this.barLength);
        this.textSize = (int) a.getDimension(13, (float) this.textSize);
        this.textColor = a.getColor(12, this.textColor);
        if (a.hasValue(11)) {
            setText(a.getString(11));
        }
        this.rimColor = a.getColor(8, this.rimColor);
        this.circleColor = a.getColor(3, this.circleColor);
        this.contourColor = a.getColor(4, this.contourColor);
        this.contourSize = a.getDimension(5, this.contourSize);
        a.recycle();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.circlePaint);
        canvas.drawArc(this.circleBounds, 360.0f, 360.0f, false, this.rimPaint);
        canvas.drawArc(this.circleOuterContour, 360.0f, 360.0f, false, this.contourPaint);
        canvas.drawArc(this.circleInnerContour, 360.0f, 360.0f, false, this.contourPaint);
        if (this.isSpinning) {
            canvas.drawArc(this.circleBounds, (float) (this.progress - 90), (float) this.barLength, false, this.barPaint);
        } else {
            canvas.drawArc(this.circleBounds, -90.0f, (float) this.progress, false, this.barPaint);
        }
        float verticalTextOffset = ((this.textPaint.descent() - this.textPaint.ascent()) / 2.0f) - this.textPaint.descent();
        for (String s : this.splitText) {
            canvas.drawText(s, ((float) (getWidth() / 2)) - (this.textPaint.measureText(s) / 2.0f), ((float) (getHeight() / 2)) + verticalTextOffset, this.textPaint);
        }
        float verticalTextOffset2 = ((this.titlePaint.descent() - this.titlePaint.ascent()) / 2.0f) - this.titlePaint.descent();
        for (String s2 : this.splitTitle) {
            canvas.drawText(s2, ((float) (getWidth() / 2)) - (this.titlePaint.measureText(s2) / 2.0f), ((float) (getHeight() / 4)) + verticalTextOffset2, this.titlePaint);
        }
        canvas.drawBitmap(this.mCurrentRunningState, (float) ((getWidth() / 2) - (this.mCurrentRunningState.getWidth() / 2)), (float) (((getHeight() / 2) - (this.mCurrentRunningState.getHeight() / 2)) + (getHeight() / 4)), this.barPaint);
    }

    public void setPlayPauseListener(PlayPauseListener listener) {
        this.mPlayPauseListener = listener;
    }

    public boolean isSpinning() {
        return this.isSpinning;
    }

    public void resetCount() {
        this.progress = 0;
        setText("0%");
        invalidate();
    }

    public void stopSpinning() {
        this.isSpinning = false;
        this.progress = 0;
        this.spinHandler.removeMessages(0);
    }

    public void spin() {
        this.isSpinning = true;
        this.spinHandler.sendEmptyMessage(0);
    }

    public void incrementProgress() {
        this.isSpinning = false;
        this.progress++;
        if (this.progress > 360) {
            this.progress = 0;
        }
        this.spinHandler.sendEmptyMessage(0);
    }

    public void setProgress(int i) {
        this.isSpinning = false;
        this.progress = i;
        this.spinHandler.sendEmptyMessage(0);
    }

    public int getProgress() {
        return this.progress;
    }

    public void setText(String text) {
        this.text = text;
        this.splitText = this.text.split("\n");
    }

    public void setTitle(String title) {
        this.title = title;
        this.splitTitle = this.title.split("\n");
    }

    public int getCircleRadius() {
        return this.circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return this.barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return this.barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextSize() {
        return this.textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return this.barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getCircleColor() {
        return this.circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getRimColor() {
        return this.rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }

    public Shader getRimShader() {
        return this.rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpinSpeed() {
        return this.spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return this.rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    public int getDelayMillis() {
        return this.delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }
}
