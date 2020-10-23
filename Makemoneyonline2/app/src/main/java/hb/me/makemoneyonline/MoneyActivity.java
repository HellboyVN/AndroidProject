package hb.me.makemoneyonline;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Random;

import hb.me.makemoneyonline.Utils.Constant;
import hb.me.makemoneyonline.basic.BasicTipFragment;
import hb.me.makemoneyonline.tip.IntroActivity;


public class MoneyActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;

    public static int navItemIndex = 1;
    public static String CURRENT_TAG = "NA";
    private String[] activityTitles;
    private Handler mHandler;

    public static LinkedList<Intent> download_queue = new LinkedList<>();
    public static String packageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(PreferenceManager.getDefaultSharedPreferences(this).getInt("theme", Constant.theme));
        setContentView(R.layout.activity_main);
        if (getAppIntro(this).booleanValue()) {
            startActivity(new Intent(this, IntroActivity.class));
        }
        initAdmob();
        Constant.showFAd(this);
        packageName = getApplicationContext().getPackageName();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(PreferenceManager.getDefaultSharedPreferences(this).getInt("color",Constant.color));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        mHandler = new Handler();
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = activityTitles[0];
            loadFragment();
        }
    }
    public Boolean getAppIntro(Context context) {
        return Boolean.valueOf(context.getSharedPreferences(Constant.MyPREFERENCES, 0).getBoolean("AppIntro", true));
    }
    Random random = new Random();

    void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_basic_page:
                        navItemIndex = 0;
                        CURRENT_TAG = activityTitles[navItemIndex];
                        break;
                    case R.id.nav_advance_page:
                        navItemIndex = 1;
                        CURRENT_TAG = activityTitles[navItemIndex];
                        break;
                    case R.id.setting_page:
                        navItemIndex = 2;
                        CURRENT_TAG = activityTitles[navItemIndex];
                        break;
                    case R.id.nav_rate_app:
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
                        } catch (ActivityNotFoundException e3) {
                            Toast.makeText(MoneyActivity.this, getResources().getString(R.string.market_error), Toast.LENGTH_SHORT).show();
                        }
                        if (drawerLayout.isShown()) drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_feed_back:

                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("message/rfc822");
                        intent.putExtra("android.intent.extra.EMAIL", new String[]{getString(R.string.developer_mail)});
                        intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.feedback_subject));
                        intent.putExtra("android.intent.extra.TEXT", "");
                        try {
                            startActivity(Intent.createChooser(intent, getResources().getString(R.string.send_email)));
                        } catch (ActivityNotFoundException e4) {
                            Toast.makeText(MoneyActivity.this, getResources().getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
                        }
                        if (drawerLayout.isShown()) drawerLayout.closeDrawers();
                        return true;

                    case R.id.nav_share:
                        Intent intent2 = new Intent("android.intent.action.SEND");
                        intent2.setType("text/plain");
                        intent2.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.share_content) + ": " + "http://play.google.com/store/apps/details?id=" + packageName);
                        startActivity(Intent.createChooser(intent2, getResources().getString(R.string.share)));
                        if (drawerLayout.isShown()) drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_fb_app:
                        try {
                            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=hb.me.giphy")));
                        } catch (ActivityNotFoundException e3) {
                            Toast.makeText(MoneyActivity.this, getResources().getString(R.string.market_error), Toast.LENGTH_SHORT).show();
                        }
                        if (drawerLayout.isShown()) drawerLayout.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }
                item.setChecked(!item.isChecked());
                if (random.nextInt(5) % 2 == 0) {

                    admob.showInterstitialAd(new Admob.IAdListener() {
                        @Override
                        public void onAdClosed() {
                            loadFragment();
                        }
                    });
                } else {
                    loadFragment();
                }
                return true;
            }
        });
    }

    private void loadFragment() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            return;
        }
        if (drawerLayout.isShown()) drawerLayout.closeDrawers();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                //  fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_content, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
                invalidateOptionsMenu();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.postDelayed(mPendingRunnable, 300);
        }
    }

    private Fragment getHomeFragment() {
        Fragment fragment;
        switch (navItemIndex) {
            case 0:
                fragment = new BasicTipFragment();
                return fragment;
            case 1:
                fragment = new HottestFragment();
                return fragment;
//            case 2:
//                fragment = new TagFragment();
//                return fragment;
//            case 3:
//                fragment = new SettingFragment();
//                return fragment;
        }

        return new SettingsFragment();

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return super.onKeyDown(keyCode, event);
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(getString(R.string.app_name));
        alert.setIcon(R.mipmap.ic_launcher);
        alert.setMessage("Are You Sure You Want To Quit?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        alert.setNegativeButton("Rate App", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String appName = getPackageName();
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + appName)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + appName)));
                }
            }
        });
        alert.show();

        return true;
    }


    Admob admob;
    public static boolean isAdShowedSuccess = false;

    void initAdmob() {
        admob = new Admob(this);
        admob.showInterstitialAd(null);
        admob.adBanner((LinearLayout) findViewById(R.id.ad_linear));
    }

    @Override
    public void onPause() {
        if (admob != null) {
            admob.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (admob != null) {
            admob.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (admob != null) {
            admob.destroy();
        }
        super.onDestroy();
    }
}
