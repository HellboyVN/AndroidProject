package lp.me.lockos10;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class LockLayer {
    private static LockLayer mLockLayer;
    private boolean isLocked;
    private Context mContext;
    private View mLockView;
    private WindowManager mWindowManager;
    private LayoutParams wmParams;

    public static synchronized LockLayer getInstance(Context mContext) {
        LockLayer lockLayer;
        synchronized (LockLayer.class) {
            if (mLockLayer == null) {
                mLockLayer = new LockLayer(mContext);
            }
            lockLayer = mLockLayer;
        }
        return lockLayer;
    }

    public LockLayer(Context mContext) {
        this.mContext = mContext;
        init();
    }

    private void init() {
        this.isLocked = false;
        this.mWindowManager = (WindowManager) this.mContext.getApplicationContext().getSystemService("window");
        this.wmParams = new LayoutParams();
        this.wmParams.type = 2010;
        this.wmParams.format = 1;
        this.wmParams.flags = 296;
        this.wmParams.width = -1;
        this.wmParams.height = -1;
    }

    public synchronized void lock() {
        if (!(this.mLockView == null || this.isLocked)) {
            this.mWindowManager.addView(this.mLockView, this.wmParams);
        }
        this.isLocked = true;
    }

    public synchronized void unlock() {
        if (this.mWindowManager != null && this.isLocked) {
            this.mWindowManager.removeView(this.mLockView);
        }
        this.isLocked = false;
    }

    public synchronized void update(int y) {
        if (!(this.mLockView == null || this.isLocked)) {
            this.wmParams.y = y;
            this.mWindowManager.updateViewLayout(this.mLockView, this.wmParams);
        }
    }

    public synchronized void setLockView(View v) {
        this.mLockView = v;
    }
}
