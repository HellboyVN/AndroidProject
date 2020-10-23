package hb.me.instagramsave.Utils;


import android.content.Context;
import android.util.Log;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;

import java.io.Serializable;

import hb.me.instagramsave.R;

public class Constant implements Serializable {
    public static com.facebook.ads.InterstitialAd interstitialAd;
    public static int color = -14606047;
    public static Boolean isNavClicked = Boolean.valueOf(false);
    public static Boolean isToggle = Boolean.valueOf(true);
    public static int nav_clicked = 0;
    public static int theme = R.style.AppTheme;
    public static void showFAd(Context context) {
        interstitialAd = new com.facebook.ads.InterstitialAd(context, "2013570418887999_2013571528887888");
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
                Log.e("---HellBoy---","Error: " + adError.getErrorMessage());
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
}
