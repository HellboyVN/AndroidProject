package hellboy.bka.tlvan.os11lockscreen;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class PullDoorView extends RelativeLayout {
    private static Handler mainHandler = null;
    private LayoutInflater inflater;
    private RelativeLayout lockScreen;
    private boolean mCloseFlag = false;
    private Context mContext;
    private int mCurryY;
    private int mDelY;
    private ImageView mImgCamera;
    private ImageView mImgView;
    private int mLastDownY = 0;
    private int mScreenHeigh = 0;
    private int mScreenWidth = 0;
    private Scroller mScroller;
    private int musicId;
    private SoundPool soundPool;
    private RelativeLayout statusbar;

    public PullDoorView(Context context) {
        super(context);
        this.mContext = context;
        setupView();
    }

    public PullDoorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setupView();
    }

    private void setupView() {
        this.inflater = LayoutInflater.from(this.mContext);
        this.statusbar = (RelativeLayout) this.inflater.inflate(R.layout.statusbar, null);
        this.lockScreen = (RelativeLayout) this.inflater.inflate(R.layout.fragment_lock, null);
        this.mScroller = new Scroller(this.mContext, new BounceInterpolator());
        WindowManager wm = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        this.mScreenHeigh = dm.heightPixels;
        this.mScreenWidth = dm.widthPixels;
        setBackgroundColor(getResources().getColor(R.color.color_text_title));
        this.mImgView = new ImageView(this.mContext);
        this.mImgView.setLayoutParams(new LayoutParams(-1, -1));
        this.mImgView.setScaleType(ScaleType.FIT_XY);
        this.mImgView.setImageResource(R.drawable.b3);
        addView(this.mImgView);
        LayoutParams params = new LayoutParams(-2, -2);
        params.addRule(12, 1);
        params.addRule(11, 1);
        this.mImgCamera = new ImageView(this.mContext);
        addView(this.mImgCamera, params);
        this.mImgCamera.setImageResource(R.drawable.camera_btn);
        addView(this.lockScreen);
        addView(this.statusbar);
    }

    public void setBgImage(int id) {
        this.mImgView.setImageResource(id);
    }

    public void setBgImage(Drawable drawable) {
        this.mImgView.setImageDrawable(drawable);
    }

    public void startBounceAnim(int startY, int dy, int duration) {
        this.mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mLastDownY = (int) event.getY();
                System.err.println("ACTION_DOWN=" + this.mLastDownY);
                return true;
            case 1:
                this.mCurryY = (int) event.getY();
                this.mDelY = this.mCurryY - this.mLastDownY;
                if (this.mDelY < 0) {
                    if (((double) Math.abs(this.mDelY)) <= ((double) this.mScreenHeigh) / 2.5d) {
                        startBounceAnim(getScrollY(), -getScrollY(), 1000);
                        break;
                    }
                    startBounceAnim(getScrollY(), this.mScreenHeigh, 300);
                    this.mCloseFlag = true;
                    break;
                }
                break;
            case 2:
                this.mCurryY = (int) event.getY();
                System.err.println("ACTION_MOVE=" + this.mCurryY);
                this.mDelY = this.mCurryY - this.mLastDownY;
                this.lockScreen.setY((float) this.mDelY);
                postInvalidate();
                postInvalidate();
                System.err.println("-------------  " + this.mDelY);
                break;
        }
        return super.onTouchEvent(event);
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
            Log.i("scroller", "getCurrX()= " + this.mScroller.getCurrX() + "     getCurrY()=" + this.mScroller.getCurrY() + "  getFinalY() =  " + this.mScroller.getFinalY());
            postInvalidate();
        } else if (this.mCloseFlag) {
            mainHandler.obtainMessage(LockActivity.MSG_LOCK_SUCESS).sendToTarget();
            setVisibility(GONE);
        }
    }

    public static void setMainHandler(Handler handler) {
        mainHandler = handler;
    }
}
