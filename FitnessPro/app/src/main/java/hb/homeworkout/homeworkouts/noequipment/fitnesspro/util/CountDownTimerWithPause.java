package hb.homeworkout.homeworkouts.noequipment.fitnesspro.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimerWithPause {
    private static final int MSG = 1;
    private final long mCountdownInterval;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            synchronized (CountDownTimerWithPause.this) {
                long millisLeft = CountDownTimerWithPause.this.timeLeft();
                if (millisLeft <= 0) {
                    CountDownTimerWithPause.this.cancel();
                    CountDownTimerWithPause.this.onFinish();
                } else if (millisLeft < CountDownTimerWithPause.this.mCountdownInterval) {
                    sendMessageDelayed(obtainMessage(1), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    CountDownTimerWithPause.this.onTick(millisLeft);
                    long delay = CountDownTimerWithPause.this.mCountdownInterval - (SystemClock.elapsedRealtime() - lastTickStart);
                    while (delay < 0) {
                        delay += CountDownTimerWithPause.this.mCountdownInterval;
                    }
                    sendMessageDelayed(obtainMessage(1), delay);
                }
            }
        }
    };
    private long mMillisInFuture;
    private long mPauseTimeRemaining;
    private boolean mRunAtStart;
    private long mStopTimeInFuture;
    private final long mTotalCountdown;

    public abstract void onFinish();

    public abstract void onTick(long j);

    public CountDownTimerWithPause(long millisOnTimer, long countDownInterval, boolean runAtStart) {
        this.mMillisInFuture = millisOnTimer;
        this.mTotalCountdown = this.mMillisInFuture;
        this.mCountdownInterval = countDownInterval;
        this.mRunAtStart = runAtStart;
    }

    public final void cancel() {
        this.mHandler.removeMessages(1);
    }

    public final synchronized CountDownTimerWithPause create() {
        if (this.mMillisInFuture <= 0) {
            onFinish();
        } else {
            this.mPauseTimeRemaining = this.mMillisInFuture;
        }
        if (this.mRunAtStart) {
            resume();
        }
        return this;
    }

    public void pause() {
        if (isRunning()) {
            this.mPauseTimeRemaining = timeLeft();
            cancel();
        }
    }

    public void resume() {
        if (isPaused()) {
            this.mMillisInFuture = this.mPauseTimeRemaining;
            this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mMillisInFuture;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
            this.mPauseTimeRemaining = 0;
        }
    }

    public boolean isPaused() {
        return this.mPauseTimeRemaining > 0;
    }

    public boolean isRunning() {
        return !isPaused();
    }

    public long timeLeft() {
        if (isPaused()) {
            return this.mPauseTimeRemaining;
        }
        long millisUntilFinished = this.mStopTimeInFuture - SystemClock.elapsedRealtime();
        if (millisUntilFinished < 0) {
            return 0;
        }
        return millisUntilFinished;
    }

    public long totalCountdown() {
        return this.mTotalCountdown;
    }

    public long timePassed() {
        return this.mTotalCountdown - timeLeft();
    }

    public boolean hasBeenStarted() {
        return this.mPauseTimeRemaining <= this.mMillisInFuture;
    }
}
