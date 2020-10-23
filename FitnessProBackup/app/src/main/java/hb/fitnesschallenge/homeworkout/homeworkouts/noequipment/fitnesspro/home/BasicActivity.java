package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import butterknife.BindView;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabhelper.SlidingTabLayout;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.iab.InAppActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.Levels;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.LevelUtils;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RestartAppModel;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RestartAppModel.OnAppRestartListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;

public class BasicActivity extends InAppActivity implements OnAppRestartListener, ObservableScrollViewCallbacks {
    public Levels datas;
    private int mBaseTranslationY;
    @BindView(R.id.header)
    LinearLayout mHeaderView;
    @BindView(R.id.pager)
    ViewPager mPager;
    public TabsNavigationAdapter mPagerAdapter;
    @BindView(R.id.mainToolbar)
    Toolbar mToolbarView;
    Typeface rLight;
    Typeface rRegular;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout slidingTabLayout;

    public Levels getDatas() {
        return this.datas;
    }

    public void setDatas(Levels datas) {
        this.datas = datas;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (SharedPrefsService.getInstance().getGender(this) == 1) {
//            startActivity(new Intent(this, OptionsActivity.class));
//            finish();
//        }
        RestartAppModel.getInstance().setListener(this);
        initFont();
        this.datas = LevelUtils.getLevelsData(this);
    }

    protected void updateRemoveAdsUI() {
    }

    protected void setWaitScreen(boolean set) {
    }

    private void initFont() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(this);
        setFont();
    }

    private void setFont() {
    }

    public void restartApp() {
        recreate();
    }

    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (dragging) {
            int toolbarHeight = this.mToolbarView.getHeight();
            float currentHeaderTranslationY = ViewHelper.getTranslationY(this.mHeaderView);
            if (firstScroll && ((float) (-toolbarHeight)) < currentHeaderTranslationY) {
                this.mBaseTranslationY = scrollY;
            }
            float headerTranslationY = ScrollUtils.getFloat((float) (-(scrollY - this.mBaseTranslationY)), (float) (-toolbarHeight), 0.0f);
            ViewPropertyAnimator.animate(this.mHeaderView).cancel();
            ViewHelper.setTranslationY(this.mHeaderView, headerTranslationY);
        }
    }

    public void onDownMotionEvent() {
    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        this.mBaseTranslationY = 0;
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            View view = fragment.getView();
            if (view != null) {
                adjustToolbar(scrollState, view);
            }
        }
    }

    protected void adjustToolbar(ScrollState scrollState, View view) {
        int toolbarHeight = this.mToolbarView.getHeight();
        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scrollable);
        if (scrollView != null) {
            int scrollY = scrollView.getCurrentScrollY();
            if (scrollState == ScrollState.DOWN) {
                showToolbar();
            } else if (scrollState == ScrollState.UP) {
                if (toolbarHeight <= scrollY) {
                    hideToolbar();
                } else {
                    showToolbar();
                }
            } else if (toolbarIsShown() || toolbarIsHidden()) {
                propagateToolbarState(toolbarIsShown());
            } else {
                showToolbar();
            }
        }
    }

    protected Fragment getCurrentFragment() {
        return this.mPagerAdapter.getItemAt(this.mPager.getCurrentItem());
    }

    protected void propagateToolbarState(boolean isShown) {
        int i;
        int toolbarHeight = this.mToolbarView.getHeight();
        TabsNavigationAdapter tabsNavigationAdapter = this.mPagerAdapter;
        if (isShown) {
            i = 0;
        } else {
            i = toolbarHeight;
        }
        tabsNavigationAdapter.setScrollY(i);
        for (int i2 = 0; i2 < this.mPagerAdapter.getCount(); i2++) {
            if (i2 != this.mPager.getCurrentItem()) {
                Fragment f = this.mPagerAdapter.getItemAt(i2);
                if (f != null) {
                    View view = f.getView();
                    if (view != null) {
                        propagateToolbarState(isShown, view, toolbarHeight);
                    }
                }
            }
        }
    }

    protected void propagateToolbarState(boolean isShown, View view, int toolbarHeight) {
        Scrollable scrollView = (Scrollable) view.findViewById(R.id.scrollable);
        if (scrollView != null) {
            if (isShown) {
                if (scrollView.getCurrentScrollY() > 0) {
                    scrollView.scrollVerticallyTo(0);
                }
            } else if (scrollView.getCurrentScrollY() < toolbarHeight) {
                scrollView.scrollVerticallyTo(toolbarHeight);
            }
        }
    }

    protected boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(this.mHeaderView) == 0.0f;
    }

    protected boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(this.mHeaderView) == ((float) (-this.mToolbarView.getHeight()));
    }

    protected void showToolbar() {
        if (ViewHelper.getTranslationY(this.mHeaderView) != 0.0f) {
            ViewPropertyAnimator.animate(this.mHeaderView).cancel();
            ViewPropertyAnimator.animate(this.mHeaderView).translationY(0.0f).setDuration(200).start();
        }
        propagateToolbarState(true);
    }

    protected void hideToolbar() {
        float headerTranslationY = ViewHelper.getTranslationY(this.mHeaderView);
        int toolbarHeight = this.mToolbarView.getHeight();
        if (headerTranslationY != ((float) (-toolbarHeight))) {
            ViewPropertyAnimator.animate(this.mHeaderView).cancel();
            ViewPropertyAnimator.animate(this.mHeaderView).translationY((float) (-toolbarHeight)).setDuration(200).start();
        }
        propagateToolbarState(false);
    }
}
