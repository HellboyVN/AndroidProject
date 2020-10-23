package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

public class BaseActivity extends AppCompatActivity {
    private AdView adView;
    protected InterstitialAd interstitialAd;
    public Toolbar toolbar;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

    public void loadBannerAdvertisement(Activity context) {
        String adId = "ca-app-pub-8468661407843417/4471893404";//real
//        String adId = "ca-app-pub-3940256099942544/6300978111";//test
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

    public void loadInterstitialAds() {
        String adId = "ca-app-pub-8468661407843417/9337207274";
            this.interstitialAd = new InterstitialAd(this);
            this.interstitialAd.setAdUnitId(adId);
            this.interstitialAd.loadAd(AppUtil.getAdRequest());
        Log.e("----adsFull----", "Loaded");
    }

    public void showInterstitialAds() {
        if (!PersistenceManager.isAdsFreeVersion()) {
            String adId = "ca-app-pub-8468661407843417/9337207274";//real
//            String adId = "ca-app-pub-3940256099942544/1033173712";//test
            this.interstitialAd = new InterstitialAd(this);
            this.interstitialAd.setAdUnitId(adId);
            this.interstitialAd.loadAd(AppUtil.getAdRequest());
            this.interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    interstitialAd.show();
                }
            });
            this.interstitialAd.loadAd(AppUtil.getAdRequest());
            Log.e("----adsFull----", "showed");

            }
        }

}
