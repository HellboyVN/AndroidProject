package com.tlvanelearning.ielts;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.vending.util.IabBroadcastReceiver;
import com.android.vending.util.IabHelper;
import com.android.vending.util.IabHelper.OnIabPurchaseFinishedListener;
import com.android.vending.util.IabHelper.OnIabSetupFinishedListener;
import com.android.vending.util.IabHelper.QueryInventoryFinishedListener;
import com.android.vending.util.IabResult;
import com.android.vending.util.Inventory;
import com.android.vending.util.Purchase;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.tlvanelearning.ielts.common.Config;
import com.tlvanelearning.ielts.fragment.TabFragment;
import com.tlvanelearning.ielts.parent.BaseActivity;
import com.tlvanelearning.ielts.player.PlayerListener.CallbackActivityListener;
import com.tlvanelearning.ielts.player.PlayerService;
import com.tlvanelearning.ielts.player.PlayerService.PlayerServiceBinder;
import com.tlvanelearning.ielts.util.PrefUtil;

import org.codechimp.apprater.AppRater;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabActivity extends BaseActivity implements IabBroadcastReceiver.IabBroadcastListener {
    private String TAG = "IAP";
    @BindView(R.id.adsView)
    LinearLayout adsView;
    private boolean doubleBackToExitPressedOnce;
    private IabHelper iabHelper;
    IabBroadcastReceiver mBroadcastReceiver;
    private boolean mBounded;
    TabLayout tablayout;
    private InterstitialAd interstitialAd;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            ((PlayerServiceBinder) service).getService().registerCallbackActivityListener(new CallbackActivityListener() {
                public void onKill() {
                    finish();
                }
            });
            mBounded = true;
        }

        public void onServiceDisconnected(ComponentName componentName) {
            mBounded = false;
        }
    };
    OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (iabHelper != null && !result.isFailure() && verifyDeveloperPayload(purchase) && purchase.getSku().equals(Config.ITEM_SKU)) {
                removeAdsVersion = true;
                PrefUtil.saveBoolean(TabActivity.this, Config.ITEM_SKU, true);
                disableAds();
            }
        }
    };
    QueryInventoryFinishedListener mReceivedInventoryListener = new QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (iabHelper != null) {
                if (result.isFailure()) {
                    Toast.makeText(TabActivity.this, "" +
                            "d.levan." + result, Toast.LENGTH_LONG).show();
                    Log.e("d.levan.", String.valueOf(result));
                    return;
                }
                Purchase premiumPurchase = inventory.getPurchase(Config.ITEM_SKU);
                TabActivity tabActivity = TabActivity.this;
                boolean z = premiumPurchase != null && verifyDeveloperPayload(premiumPurchase);
                tabActivity.removeAdsVersion = z;
                Log.d(TAG, "User is " + (removeAdsVersion ? "PREMIUM" : "NOT PREMIUM"));
                if (removeAdsVersion) {
                    PrefUtil.saveBoolean(context, Config.ITEM_SKU, true);
                    disableAds();
                    return;
                }
                facebookBanner(context, adsView);
            }
        }
    };
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private boolean removeAdsVersion;

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            iabHelper.queryInventoryAsync(mReceivedInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            Toast.makeText(getApplicationContext(),"Error querying inventory. Another async operation in progress.",Toast.LENGTH_SHORT).show();
        }
    }

    public interface FragmentCallback {
        void callback();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private FragmentCallback fragmentCallback = new FragmentCallback() {
            public void callback() {
                showAdmobInterstitialAd();
            }
        };

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            tablayout.getTabAt(0).setIcon(R.drawable.level1);
            tablayout.getTabAt(1).setIcon(R.drawable.level2);
            tablayout.getTabAt(2).setIcon(R.drawable.level3);
            tablayout.getTabAt(3).setIcon(R.drawable.headphone1);
            return TabFragment.newInstance(position + 1, this.fragmentCallback);
        }

        public int getCount() {
            return 4;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "BASIC";
                case 1:
                    return "INTER";
                case 2:
                    return "ADV";
                case 3:
                    return "MORE";
                default:
                    return null;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        ButterKnife.bind((Activity) this);
        loadIAP();
        showFAd();
        initService();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        this.mViewPager = (ViewPager) findViewById(R.id.container);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        tablayout =  (TabLayout) findViewById(R.id.tabs);
        tablayout.setupWithViewPager(this.mViewPager);

//        key();
        AppRater.app_launched(this);
    }
    public void showFAd() {
        Log.e("facebook_start","----HERE-----");
        interstitialAd = new InterstitialAd(this, "2001659010117857_2011796662437425");
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                LogUtil.d("Error: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Show the ad when it's done loading.
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }
//    private void key(){
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.tlvanelearning.ielts",
//                    PackageManager.GET_SIGNATURES);
//            for (android.content.pm.Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
//    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.iabHelper != null && !this.iabHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_purchase:
                try {
                    this.iabHelper.launchPurchaseFlow(this, Config.ITEM_SKU, 10001, this.mPurchaseFinishedListener, "mypurchasetoken");
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        if (this.doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            if (mBroadcastReceiver != null) {
                unregisterReceiver(mBroadcastReceiver);
            }
            if (this.mBounded) {
                PlayerService playerService = PlayerService.getInstance();
                if (playerService != null) {
                    playerService.stopService();
                }
                unbindService(this.mConnection);
            }
            try {
                if (this.iabHelper != null) {
                    this.iabHelper.dispose();
                }
                this.iabHelper = null;
            } catch (Exception e) {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void disableAds() {
        try {
            this.adsView.setVisibility(View.GONE);
            this.adsView.setVisibility(View.INVISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initService() {
        if (this.mBounded) {
            this.mBounded = true;
        } else {
            startJcPlayerService();
        }
    }

    private synchronized void startJcPlayerService() {
        if (!this.mBounded) {
            Intent intent = new Intent(this.context.getApplicationContext(), PlayerService.class);
            Context context = this.context;
            ServiceConnection serviceConnection = this.mConnection;
            this.context.getApplicationContext();
            context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void loadIAP() {
        this.iabHelper = new IabHelper(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArSOK9o/c9fGcgn1sjFCLOqOnhyetOcmR9NaTWcVCQNw+LSOGYBly0fYAIPtA/gZUJiJsfrK+MBTEzZ7Y7CbQZMllSpf2RSWQCdgOCFrg9JUleQvLp5qdSYy89SPrb87INYdRifXgAQsUebQfdERm8EtRJ5YICmFxKgMeuNICyfRrq+JAhNlXbATpBHLLVYiHho2wwqOq1kJMi+kuZdYsk2bUsB4jj/ICEMWrQGbvV/1D3nFzfIpB8vvtiGcUQqTu7dkhX4ClUAA2/WLAKAyMiJZ1Ecu2hVq8fS+aWuzmEVKOArXeHbjmoJ1JpngBGB94WvGnshtxpVs7M8EyNHYkIQIDAQAB");
        this.iabHelper.enableDebugLogging(true);
        this.iabHelper.startSetup(new OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Toast.makeText(TabActivity.this, "Problem setting up in-app billing: " + result, Toast.LENGTH_LONG).show();
                } else if (iabHelper != null) {
                    try {
                        mBroadcastReceiver = new IabBroadcastReceiver(TabActivity.this);
                        IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                        registerReceiver(mBroadcastReceiver, broadcastFilter);
                        iabHelper.queryInventoryAsync(mReceivedInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }
}
