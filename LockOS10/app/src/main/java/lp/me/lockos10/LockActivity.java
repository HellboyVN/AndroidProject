package lp.me.lockos10;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import lp.me.lockos10.fragment.FragmentLock;

public class LockActivity extends Activity implements OnTouchListener {
//    public static int MSG_LOCK_SUCESS = 291;
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        finish();
//    }
//
//    public boolean onTouch(View v, MotionEvent event) {
//        return true;
//    }
//}
public static int MSG_LOCK_SUCESS = 291;
    public static int UPDATE_TIME = 564;
    private PagerAdapter adapter;
    private ImageView btnCamera;
    private FragmentLock fragmentLock;
    private FragmentLock fragmentLock2;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == LockActivity.MSG_LOCK_SUCESS) {
                LockActivity.this.lockLayer.unlock();
                LockActivity.this.finish();
            }
        }
    };
    private LockLayer lockLayer;
    private PullDoorView pullDoorView;
    private RelativeLayout screen;
    private Scroller scroller;
    private Shimmer shimmer;
    private ShimmerTextView shimmerTextView;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        this.pullDoorView = new PullDoorView(this);
        finish();
        this.shimmerTextView = (ShimmerTextView) this.pullDoorView.findViewById(R.id.shimmer_tv);
    }

    private void setView() {
        this.scroller = new Scroller(this, new BounceInterpolator());
    }

    public void startBounceAnim(int startY, int dy, int duration) {
        this.scroller.startScroll(0, startY, 0, dy, duration);
        this.screen.invalidate();
    }

    private void startShimer() {
        this.shimmer = new Shimmer();
        this.shimmer.setRepeatCount(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION).setDuration(800).setStartDelay(300).setDirection(0).setAnimatorListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.shimmer.start(this.shimmerTextView);
    }

    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}