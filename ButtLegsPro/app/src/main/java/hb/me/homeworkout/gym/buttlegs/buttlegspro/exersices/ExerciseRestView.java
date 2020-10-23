package hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRestCountDown;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SoundHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class ExerciseRestView extends AppCompatTextView {
    private final long GO_TIME = 1500;
    private long MANUAL_NAVIGATION_OFFSET = 3000;
    private final long READY_TIME = 3000;
    private long REST_DELTA = 5000;
    private long REST_LABEL_TIME_OFFSET = 2000;
    private long REST_LIMIT = 20000;
    private long REST_TIME_DEFAULT = (15000 + this.REST_LABEL_TIME_OFFSET);
    private long REST_TIME_FIRST_PAGE = 4000;
    private FloatingActionButton mAddTime;
    private IRestCountDown mIRestCountDown;
    private boolean mIsLongRest;
    private boolean mPlayGo;
    private boolean mPlayReady;
    private CountDownTimer mRestCountDownTimer;
    private long mRestTime;
    private Toast mToast = null;

    public ExerciseRestView(Context context) {
        super(context);
    }

    public ExerciseRestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExerciseRestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(IRestCountDown iRestCountDown, FloatingActionButton button, boolean isManual) {
        this.mIRestCountDown = iRestCountDown;
        this.mAddTime = button;
        if (isManual) {
            long j = this.MANUAL_NAVIGATION_OFFSET;
            this.REST_TIME_FIRST_PAGE = j;
            this.REST_TIME_DEFAULT = j;
        } else {
            this.REST_TIME_DEFAULT = ((long) (SharedPrefsService.getInstance().getRestTime(getContext()) * 1000)) + this.REST_LABEL_TIME_OFFSET;
            this.REST_TIME_FIRST_PAGE = (long) (SharedPrefsService.getInstance().getReadyTime(getContext()) * 1000);
        }
        setTypeface(TypeFaceService.getInstance().getRobotoRegular(getContext()));
        this.mAddTime.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExerciseRestView.this.addRestTime();
            }
        });
    }

    public void startRest(boolean isFirstPage, boolean isManual) {
        if (isFirstPage) {
            this.mRestTime = this.REST_TIME_FIRST_PAGE;
        } else {
            if (isManual) {
                long j = this.MANUAL_NAVIGATION_OFFSET;
                this.REST_TIME_FIRST_PAGE = j;
                this.REST_TIME_DEFAULT = j;
            } else {
                this.REST_TIME_DEFAULT = ((long) (SharedPrefsService.getInstance().getRestTime(getContext()) * 1000)) + this.REST_LABEL_TIME_OFFSET;
                this.REST_TIME_FIRST_PAGE = (long) (SharedPrefsService.getInstance().getReadyTime(getContext()) * 1000);
                setText(getContext().getString(R.string.rest));
                playSound("rest");
            }
            this.mRestTime = this.REST_TIME_DEFAULT;
        }
        createTimer(this.mRestTime);
        this.mIsLongRest = false;
    }

    public void setRestTime(long restTime) {
        this.mRestTime = restTime;
    }

    public long getRestTime() {
        return this.mRestTime;
    }

    public void addRestTime() {
        if (this.mRestTime >= this.REST_LIMIT || this.mIsLongRest) {
            this.mIsLongRest = true;
            String st = getContext().getString(R.string.dialog_long_rest);
            if (this.mToast != null) {
                this.mToast.cancel();
            }
            this.mToast = Toast.makeText(getContext(), st, 0);
            this.mToast.show();
            return;
        }
        this.mRestTime += this.REST_DELTA;
        updateRestTimer();
    }

    private void createTimer(long restTime) {
        this.mPlayReady = true;
        this.mPlayGo = true;
        this.mRestCountDownTimer = new CountDownTimer(restTime, 100) {
            public void onTick(long millisUntilFinished) {
                ExerciseRestView.this.mRestTime = millisUntilFinished;
                if (millisUntilFinished <= 3000 && millisUntilFinished > 1500) {
                    if (ExerciseRestView.this.mPlayReady) {
                        ExerciseRestView.this.playSound("ready");
                        ExerciseRestView.this.mPlayReady = false;
                    }
                    ExerciseRestView.this.setText(ExerciseRestView.this.getContext().getString(R.string.ready));
                    if (ExerciseRestView.this.mAddTime.isShown()) {
                        ExerciseRestView.this.mAddTime.hide();
                    }
                } else if (millisUntilFinished <= 1500) {
                    if (ExerciseRestView.this.mPlayGo) {
                        ExerciseRestView.this.playSound("go");
                        ExerciseRestView.this.mPlayGo = false;
                    }
                    ExerciseRestView.this.setText(ExerciseRestView.this.getContext().getString(R.string.go));
                } else if (ExerciseRestView.this.REST_TIME_DEFAULT - millisUntilFinished > ExerciseRestView.this.REST_LABEL_TIME_OFFSET || ExerciseRestView.this.mAddTime.isShown()) {
                    if (!ExerciseRestView.this.mAddTime.isShown()) {
                        ExerciseRestView.this.mAddTime.show();
                    }
                    ExerciseRestView.this.setText(String.valueOf(millisUntilFinished / 1000));
                }
            }

            public void onFinish() {
                ExerciseRestView.this.mIRestCountDown.onFinish();
                ExerciseRestView.this.mPlayReady = true;
                ExerciseRestView.this.mPlayGo = true;
            }
        };
        this.mRestCountDownTimer.start();
    }

    public void cancelTimer() {
        if (this.mRestCountDownTimer != null) {
            this.mRestCountDownTimer.cancel();
            this.mRestCountDownTimer = null;
        }
    }

    public void updateRestTimer() {
        if (this.mRestCountDownTimer != null) {
            this.mRestCountDownTimer.cancel();
        }
        createTimer(this.mRestTime);
    }

    public void pauseRestTimer() {
        if (this.mRestCountDownTimer != null) {
            this.mRestCountDownTimer.cancel();
        }
    }

    void playSound(String resource) {
        if (SharedPrefsService.getInstance().getSoundStatus(getContext())) {
            SoundHelper.getInstance().playSound(getContext(), resource);
        }
    }
}
