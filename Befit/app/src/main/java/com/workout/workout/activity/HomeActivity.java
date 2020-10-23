package com.workout.workout.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ShareEvent;
import com.workout.workout.R;
import com.workout.workout.adapter.ViewPagerAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.util.AppRate;
import com.workout.workout.util.BottomNavigationViewHelper;

import java.util.List;

public class HomeActivity extends BaseActivity implements PurchasesUpdatedListener {
    private BottomNavigationView bottomNavigationView;
    boolean doubleBackToExitPressedOnce = false;
    private BillingClient mBillingClient;
    private OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_training:
                    HomeActivity.this.setViewPagerPage(0);
                    HomeActivity.this.setToolbarTitle("Workout Training");
                    return true;
                case R.id.navigation_plan:
                    HomeActivity.this.setViewPagerPage(1);
                    HomeActivity.this.setToolbarTitle("Workout Plans");
                    return true;
                case R.id.navigation_utility:
                    HomeActivity.this.setViewPagerPage(2);
                    HomeActivity.this.setToolbarTitle("Utility");
                    return true;
                case R.id.navigation_favourite:
                    HomeActivity.this.setViewPagerPage(3);
                    HomeActivity.this.setToolbarTitle("Favourite");
                    return true;
                case R.id.navigation_more:
                    HomeActivity.this.setViewPagerPage(4);
                    HomeActivity.this.setToolbarTitle("More");
                    return true;
                default:
                    return false;
            }
        }
    };
    private MenuItem prevMenuItem;
    private ViewPager viewPager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_home);
        setToolbar("Workout Training", false);
        preparebillingClient();
        try {
            new AppRate(this).setShowIfAppHasCrashed(false).setMinDaysUntilPrompt(1).setMinLaunchesUntilPrompt(5).init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this.mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(this.bottomNavigationView);
        if (getResources().getBoolean(R.bool.is_ten_inch_tab)) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) this.bottomNavigationView.getChildAt(0);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
                LayoutParams layoutParams = iconView.getLayoutParams();
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                layoutParams.height = (int) TypedValue.applyDimension(1, 32.0f, displayMetrics);
                layoutParams.width = (int) TypedValue.applyDimension(1, 32.0f, displayMetrics);
                iconView.setLayoutParams(layoutParams);
            }
        }
        setupViewPager();
    }

    private void preparebillingClient() {
        this.mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int billingResponseCode) {
                if (billingResponseCode != 0) {
                }
            }

            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.setAdapter(adapter);
        this.viewPager.addOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                if (HomeActivity.this.prevMenuItem != null) {
                    HomeActivity.this.prevMenuItem.setChecked(false);
                } else {
                    HomeActivity.this.bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                HomeActivity.this.bottomNavigationView.getMenu().getItem(position).setChecked(true);
                HomeActivity.this.prevMenuItem = HomeActivity.this.bottomNavigationView.getMenu().getItem(position);
                switch (position) {
                    case 0:
                        HomeActivity.this.setToolbarTitle("Workout Training");
                        return;
                    case 1:
                        HomeActivity.this.setToolbarTitle("Workout Plans");
                        return;
                    case 2:
                        HomeActivity.this.setToolbarTitle("Utility");
                        return;
                    case 3:
                        HomeActivity.this.setToolbarTitle("Favourite");
                        return;
                    case 4:
                        HomeActivity.this.setToolbarTitle("More");
                        return;
                    default:
                        return;
                }
            }

            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setViewPagerPage(int page) {
        if (this.viewPager != null) {
            this.viewPager.setCurrentItem(page);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        menu.findItem(R.id.add).setVisible(false);
        menu.findItem(R.id.premium).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String charSequence = item.getTitle().toString();
        int i = -1;
        switch (charSequence.hashCode()) {
            case -1646910018:
                if (charSequence.equals("Rate us")) {
                    boolean z = true;
                    break;
                }
                break;
            case -1204488396:
                if (charSequence.equals("Remove ads")) {
                    i = 2;
                    break;
                }
                break;
            case -126857307:
                if (charSequence.equals("Feedback")) {
                    i = 3;
                    break;
                }
                break;
            case 79847359:
                if (charSequence.equals("Share")) {
                    i = 0;
                    break;
                }
                break;
            case 1649250771:
                if (charSequence.equals("Visit Us")) {
                    i = 4;
                    break;
                }
                break;
        }
        switch (i) {
            case 0:
                Intent sendIntent = new Intent();
                sendIntent.setAction("android.intent.action.SEND");
                sendIntent.putExtra("android.intent.extra.TEXT", "Download ProGym workout now! The Professional Bodybuilding App:\nhttps://play.google.com/store/apps/details?id=com.workout.workout");
                sendIntent.setType("text/plain");
                try {
                    Answers.getInstance().logShare(new ShareEvent().putMethod("App link share"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(sendIntent);
                return true;
            case 1:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=com.workout.workout")));
                    return true;
                } catch (Exception e2) {
                    Toast.makeText(this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                    return true;
                }
            case 2:
                if (PersistenceManager.isAdsFreeVersion()) {
                    Toast.makeText(this, "Ads are already removed!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                this.mBillingClient.launchBillingFlow(this, BillingFlowParams.newBuilder().setSku(AppConstants.SKU_AD_FREE).setType("inapp").build());
                return true;
            case 3:
                Intent email = new Intent("android.intent.action.SEND");
                email.putExtra("android.intent.extra.EMAIL", new String[]{"progymworkout@gmail.com"});
                email.putExtra("android.intent.extra.SUBJECT", "Feedback for ProGym Workout");
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email sender :"));
                return true;
            case 4:
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://progymworkout.com")));
                    return true;
                } catch (Exception e3) {
                    Toast.makeText(this, "Unknown Error Occured", Toast.LENGTH_SHORT).show();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                HomeActivity.this.doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
        if (responseCode == 0 && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().equalsIgnoreCase(AppConstants.SKU_AD_FREE)) {
                    Toast.makeText(this, "All ads removed!", Toast.LENGTH_LONG).show();
                    PersistenceManager.setAdsFreeVersion(true);
                }
            }
        } else if (responseCode != 1 && responseCode != -1 && responseCode != 5 && responseCode != 3 && responseCode != 6 && responseCode != -2 && responseCode != 7 && responseCode != 2 && responseCode == 4) {
        }
    }
}
