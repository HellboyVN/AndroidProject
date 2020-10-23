package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.listeners.EventCenter;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.setting.SettingActivity;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.EmailHelper;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.RateHelper;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SharedPrefsService;

public class MainActivity extends BasicActivity implements OnMenuItemClickListener {
    @BindView(R.id.mainToolbarText)
    AppCompatTextView mainToolbarText;
    private AdView mAdView;

    @OnClick({R.id.menu})
    public void openMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.activity_main_drawer);
        popupMenu.show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        Log.e("hellboy","Oncreate Main");
        ButterKnife.bind((Activity) this);
        MultiDex.install(this);
        ViewCompat.setElevation(this.mHeaderView, getResources().getDimension(R.dimen.toolbar_elevation));
        setupNavDrawer();
        if(SharedPrefsService.getInstance().getGender(this) != 0){
            showInterFacebook(MainActivity.this);
        }

        this.mAdView = (AdView) findViewById(R.id.adViewMain);
        if (isProPackagePurchased()) {
            this.mAdView.setVisibility(View.GONE);
        } else {
            this.mAdView.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        }
        this.mainToolbarText.setTypeface(this.rLight);
        this.mPagerAdapter = new TabsNavigationAdapter(getSupportFragmentManager(), getApplicationContext());
        this.mPager = (ViewPager) findViewById(R.id.pager);
        this.mPager.setOffscreenPageLimit(4);
        this.mPager.setAdapter(this.mPagerAdapter);
        this.slidingTabLayout.setCustomTabView(R.layout.tab_indicator, 16908308, R.id.tab_icon);
        this.slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        this.slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getApplicationContext(), R.color.white));
        this.slidingTabLayout.setDistributeEvenly(false);
        this.slidingTabLayout.setViewPager(this.mPager);
        this.slidingTabLayout.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int i, float v, int i2) {
            }

            public void onPageSelected(int i) {
                MainActivity.this.propagateToolbarState(MainActivity.this.toolbarIsShown());
            }

            public void onPageScrollStateChanged(int i) {
            }
        });
        propagateToolbarState(toolbarIsShown());
        updateRemoveAdsUI();

    }

    private void setupNavDrawer() {
    }

    protected void updateRemoveAdsUI() {
        if (isProPackagePurchased()) {
            EventCenter.getInstance().notifyUpdateRemoveAdsUI();
        }
    }

    protected void setWaitScreen(boolean set) {
    }

    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_share) {
            RateHelper.shareApp(getApplicationContext());
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_rate) {
            RateHelper.openPlayStore(getApplicationContext(), getPackageName(), getString(R.string.thank_you));
        } else if (id == R.id.nav_translate) {
            EmailHelper.getInstance().sendEmail("phantai.9447@gmail.com", getString(R.string.menu_help_to_translate), "I want to make some reviews: ");
        }
        return true;
    }
}
