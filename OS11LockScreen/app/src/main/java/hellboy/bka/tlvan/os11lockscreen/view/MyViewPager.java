package hellboy.bka.tlvan.os11lockscreen.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class MyViewPager extends ViewPager {
    PagerAdapter mPagerAdapter;

    public MyViewPager(Context context) {
        super(context);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mPagerAdapter != null) {
            super.setAdapter(this.mPagerAdapter);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
    }

    public void storeAdapter(PagerAdapter pagerAdapter) {
        this.mPagerAdapter = pagerAdapter;
    }
}
