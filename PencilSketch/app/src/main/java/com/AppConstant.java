package com;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.hbtools.photo.sketchpencil.editer.pencilsketch.R;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by tlvan on 2/27/2018.
 */

public class AppConstant {
    private AdView mAdView;
    private Context context;
    private InterstitialAd interstitialAd;
    public static final String TEST_DEVICE = "3F90C25D51474DDEA1C2B2319E1ADE19";
    public static final String ggpublishkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxNEBmTiYBTZJ6IuSDqV+P8d3vewAbeahVU4jxBlcHxXtFSSLRjiljQ4cvQX7LR7OOWrxOg38VjkT++HEu3bN/W9RqnDYAYg3JLablXL3hwwNMwL99hiqI+mJS/SABGBfOVbx9D9zkEhOrvZdBSsvg9ZRfFFECy1BsPla7U8OqL2ZcSizcD+k3EYjWyqpzV0NhtFV5dIMYJ0p52WZIEK34PxdDEAQgZGdrVq9uk9UvPZUDoZQiNvpCTgQNwPlVjvZf8YoyJiC6yeT3lnGTdqz5eGprFcW4txo+/kzQFLP7n40x/uUA2+lS++VcAglhJ+FNgZU2pcWMZY1pAraiMxTJQIDAQAB";
    public static final String REMOVEADS = "REMOVEADS";
    public static boolean isRemoveAds(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Boolean.valueOf(sharedPreferences.getBoolean(REMOVEADS, false));
    }
    public AppConstant(Context context){
        this.context = context;
    }
    public static void setAdsFreeVersion(Context context, boolean value1){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(REMOVEADS, value1);
        editor.apply();
    }
    public static boolean isNetWorkAvailable(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        try {
            final InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
        }
        return false;
    }
    public void adBanner(ViewGroup viewGroup) {
        if ((!isRemoveAds(context)) && isNetWorkAvailable()) {
            mAdView = new AdView(context);
            mAdView.setAdSize(AdSize.SMART_BANNER);
            mAdView.setAdUnitId(context.getResources().getString(R.string.ad_banner_gif));
            viewGroup.addView(mAdView);

            AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE).build();
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    Log.e("Load error ... ", "eror " + errorCode);
                    mAdView.setVisibility(View.GONE);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.e("Load successful ... ", "ok");
                    mAdView.setVisibility(View.VISIBLE);
                }
            });
            mAdView.loadAd(adRequest);
        }
    }
    public void showFAd() {
        if ((!isRemoveAds(context)) && isNetWorkAvailable()) {
        {
            interstitialAd = new InterstitialAd(context, "601076100230303_601078780230035");
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
                    Log.e("Error: ","Error" + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Show the ad when it's done loading.
//                    Random rand = new Random();
//                    int n = rand.nextInt(3) + 1;
//                    Log.e("levan_new", "show + n= " + String.valueOf(n));
//                    if (n == 1) {
                    Log.e("levan_new", "show ads face ");
                        interstitialAd.show();
//                    }
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
    }
    }
    public void pause() {
        if (mAdView != null) {
            mAdView.pause();
        }
//        if (mNativeExpressAdView != null) {
//            mNativeExpressAdView.pause();
//        }
    }

    public void resume() {
        if (mAdView != null) {
            mAdView.resume();
        }

//        if (mNativeExpressAdView != null) {
//            mNativeExpressAdView.resume();
//        }
    }

    public void destroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }
}
