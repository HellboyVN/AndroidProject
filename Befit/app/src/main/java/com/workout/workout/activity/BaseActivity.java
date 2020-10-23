package com.workout.workout.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.workout.workout.R;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.util.AppUtil;

import io.fabric.sdk.android.Fabric;

public class BaseActivity extends AppCompatActivity {
    private AdView adView;
    protected InterstitialAd interstitialAd;
    public Toolbar toolbar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Fabric.with(this, new Crashlytics());
    }

    public void setToolbar(String title, boolean isHomeUpEnable) {
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle((CharSequence) title);
            actionBar.setDisplayHomeAsUpEnabled(isHomeUpEnable);
            actionBar.setDisplayShowHomeEnabled(isHomeUpEnable);
        }
    }

    public void setToolbarTitle(String title) {
        if (this.toolbar != null) {
            this.toolbar.setTitle((CharSequence) title);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onResume() {
        super.onResume();
        if (this.adView != null) {
            this.adView.resume();
        }
    }

    public void onPause() {
        if (this.adView != null) {
            this.adView.pause();
        }
        super.onPause();
    }

    public void loadBannerAdvertisement(Activity context, String adId) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            this.adView = new AdView(context);
            this.adView.setAdSize(AdSize.SMART_BANNER);
            this.adView.setAdUnitId(adId);
            ((LinearLayout) findViewById(R.id.adView)).addView(this.adView);
            AdRequest adRequest = AppUtil.getAdRequest();
            if (this.adView != null) {
                this.adView.setVisibility(View.GONE);
                this.adView.loadAd(adRequest);
            }
            if (this.adView != null) {
                this.adView.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        BaseActivity.this.adView.setVisibility(View.VISIBLE);
                    }

                    public void onAdFailedToLoad(int errorCode) {
                    }

                    public void onAdOpened() {
                    }

                    public void onAdLeftApplication() {
                    }

                    public void onAdClosed() {
                    }
                });
            }
        }
    }

    protected void loadInterstitialAds(String adId) {
        if (false) {
            this.interstitialAd = new InterstitialAd(this);
            this.interstitialAd.setAdUnitId(adId);
            this.interstitialAd.loadAd(AppUtil.getAdRequest());
        }
    }

    protected void showInterstitialAds(String adId) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            if (this.interstitialAd == null || !this.interstitialAd.isLoaded()) {
                loadInterstitialAds(adId);
            } else {
                this.interstitialAd.show();
            }
        }
    }
}
