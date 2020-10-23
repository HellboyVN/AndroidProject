package hb.me.homeworkout.gym.buttlegs.buttlegspro.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.RealmManager;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.RealmManager.IUserReady;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.User;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.InAppActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.ReminderActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.setting.SettingActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.StatisticActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RateHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RestartAppModel;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RestartAppModel.OnAppRestartListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ShareHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

//import com.google.firebase.perf.metrics.AppStartTrace;

public class MainActivity extends InAppActivity implements OnNavigationItemSelectedListener, OnAppRestartListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    Dialog dialog;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.goProText1)
    AppCompatTextView goProText1;
    @BindView(R.id.goProText2)
    AppCompatTextView goProText2;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.mainContainer)
    FrameLayout mainContainer;
    @BindView(R.id.menuGoPro)
    LinearLayout menuGoPro;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.adViewContainer)
    RelativeLayout adViewContainer;

    Typeface rLight;

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.home.MainActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.home.MainActivity");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        MultiDex.install(this);
        RestartAppModel.getInstance().setListener(this);
        ButterKnife.bind((Activity) this);
        setSupportActionBar(this.mToolbar);
        RealmManager.getInstance().initUser(new IUserReady() {
            public void onUserReady(User user) {
            }
        });
        setupNavDrawer();
        //facebook
        faceBookBanner(this,adViewContainer);
        initFont();
        setupBottomBar();
        updateRemoveAdsUI();
        this.menuGoPro.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.onPurchaseClick();
            }
        });
        this.goProText1.setTypeface(TypeFaceService.getInstance().getRobotoRegular(getApplicationContext()));
        this.goProText2.setTypeface(TypeFaceService.getInstance().getRobotoMedium(getApplicationContext()));
        showInterFacebook(this);
    }

    public void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.home.MainActivity");
        super.onResume();
        EventCenter.getInstance().notifyAdapterUpdate();
    }

    private void setupBottomBar() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Fragment homeFragment = TabManager.get(R.id.action_home, false, getApplicationContext());
        if (homeFragment != null) {
            fragmentManager.beginTransaction().add((int) R.id.mainContainer, homeFragment).commitAllowingStateLoss();
        }
        final Fragment challengeFragment = TabManager.get(R.id.action_challenge, false, getApplicationContext());
        if (challengeFragment != null) {
            fragmentManager.beginTransaction().add((int) R.id.mainContainer, challengeFragment).commitAllowingStateLoss();
        }
        final Fragment tmFragment = TabManager.get(R.id.action_treadmill, false, getApplicationContext());
        if (tmFragment != null) {
            fragmentManager.beginTransaction().add((int) R.id.mainContainer, tmFragment).commitAllowingStateLoss();
        }
        switch (SharedPrefsService.getInstance().getLastTab(getApplicationContext())) {
            case 20:
                this.bottomNavigation.setSelectedItemId(R.id.action_home);
                fragmentManager.beginTransaction().hide(tmFragment).show(homeFragment).hide(challengeFragment).commitAllowingStateLoss();
                break;
            case 21:
                this.bottomNavigation.setSelectedItemId(R.id.action_treadmill);
                fragmentManager.beginTransaction().hide(homeFragment).show(tmFragment).hide(challengeFragment).commitAllowingStateLoss();
                break;
            case 22:
                this.bottomNavigation.setSelectedItemId(R.id.action_challenge);
                fragmentManager.beginTransaction().hide(homeFragment).hide(tmFragment).show(challengeFragment).commitAllowingStateLoss();
                break;
        }
        this.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_challenge:
                        fragmentManager.beginTransaction().hide(homeFragment).show(challengeFragment).hide(tmFragment).commitAllowingStateLoss();
                        SharedPrefsService.getInstance().setLastTab(MainActivity.this.getApplicationContext(), 22);
                        break;
                    case R.id.action_home:
                        fragmentManager.beginTransaction().hide(tmFragment).show(homeFragment).hide(challengeFragment).commitAllowingStateLoss();
                        SharedPrefsService.getInstance().setLastTab(MainActivity.this.getApplicationContext(), 20);
                        break;
                    case R.id.action_treadmill:
                        fragmentManager.beginTransaction().hide(homeFragment).show(tmFragment).hide(challengeFragment).commitAllowingStateLoss();
                        SharedPrefsService.getInstance().setLastTab(MainActivity.this.getApplicationContext(), 21);
                        break;
                }
                return true;
            }
        });
    }

    private void setupNavDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayout, this.mToolbar, 0, 0);
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        this.navView.setNavigationItemSelectedListener(this);
    }

    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
            this.drawerLayout.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_share) {
            ShareHelper.shareApp(getApplicationContext());
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (id == R.id.nav_rate) {
            RateHelper.openPlayStore(getApplicationContext(), "hb.me.homeworkout.gym.buttlegs.buttlegspro", getString(R.string.thank_you));
        } else if (id == R.id.nav_reminder) {
            startActivity(new Intent(this, ReminderActivity.class));
        } else if (id == R.id.nav_insta) {
            ShareHelper.shareWithInstagram(this);
        } else if (id == R.id.nav_weight) {
            startActivity(new Intent(this, StatisticActivity.class));
        }
        this.drawerLayout.closeDrawer((int) GravityCompat.START);
        return true;
    }

    private void initFont() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this);
    }

    public void restartApp() {
        recreate();
    }

    protected void updateRemoveAdsUI() {
        if (isRemoveAdsPurchased()) {
            this.menuGoPro.setVisibility(8);
            EventCenter.getInstance().notifyUpdateRemoveAdsUI();
            return;
        }
        this.menuGoPro.setVisibility(0);
    }

    protected void setWaitScreen(boolean set) {
    }

    protected void iabSetupFailed() {
    }
}
