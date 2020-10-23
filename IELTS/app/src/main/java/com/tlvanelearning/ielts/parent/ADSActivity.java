package com.tlvanelearning.ielts.parent;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tlvanelearning.ielts.common.Config;
import com.tlvanelearning.ielts.util.NetworkUtil;
import com.tlvanelearning.ielts.util.PrefUtil;

import java.util.Random;

public class ADSActivity extends AppCompatActivity {
    protected static String TAG = "DEBUG";
    private InterstitialAd adInterstitialAd;
    private AdView facebookView;
    private boolean failedToLoad = false;
    private com.facebook.ads.InterstitialAd fbInterstitialAd;

    protected void loadADS(Context context) {
        admobCreateOrLoadInterstitial(context);
        facebookCreateOrLoadInterstitial(context);
    }

    protected void admobBanner(final Context context, final LinearLayout adsView) {
        MobileAds.initialize(this, "ca-app-pub-8468661407843417~2388350348");
        try {
            if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(context)) {
                final com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(context);
                adView.setAdSize(AdSize.BANNER);
                adView.setAdUnitId(Config.ADMOB_BANNER_ADS_ID);
                adView.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        adsView.setVisibility(View.VISIBLE);
                        if (adsView.getChildCount() != 0) {
                            adsView.removeAllViews();
                        }
                        adsView.addView(adView);
                    }

                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        if (!ADSActivity.this.failedToLoad) {
                            ADSActivity.this.facebookBanner(context, adsView);
                        }
                        ADSActivity.this.failedToLoad = !ADSActivity.this.failedToLoad;
                    }
                });
                adView.loadAd(new Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
            }
        } catch (Exception e) {
        }
    }

    protected void facebookBanner(final Context context, final LinearLayout adsView) {
        try {
            if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(context)) {
                this.facebookView = new AdView(this, Config.FACEBOOK_BANNER_ADS_ID, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                this.facebookView.setAdListener(new com.facebook.ads.AdListener() {
                    public void onError(Ad ad, AdError adError) {
                        if (!ADSActivity.this.failedToLoad) {
                            ADSActivity.this.admobBanner(context, adsView);
                        }
                        ADSActivity.this.failedToLoad = !ADSActivity.this.failedToLoad;
                    }

                    public void onAdLoaded(Ad ad) {
                        adsView.setVisibility(View.VISIBLE);
                        if (adsView.getChildCount() != 0) {
                            adsView.removeAllViews();
                        }
                        adsView.addView(ADSActivity.this.facebookView);
                    }

                    public void onAdClicked(Ad ad) {
                    }

                    public void onLoggingImpression(Ad ad) {
                    }
                });
                this.facebookView.loadAd();
            }
        } catch (Exception e) {
        }
    }

    protected void admobCreateOrLoadInterstitial(final Context context) {
        try {
            if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(context)) {
                this.adInterstitialAd = new InterstitialAd(context);
                this.adInterstitialAd.setAdUnitId(Config.ADMOB_INTER_ADS_ID);
                this.adInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                    }

                    public void onAdFailedToLoad(int errorCode) {
                    }

                    public void onAdClosed() {
                        super.onAdClosed();
                        ADSActivity.this.admobCreateOrLoadInterstitial(context);
                    }
                });
                admobLoadIntestitialAd();
            }
        } catch (Exception e) {
        }
    }

    protected void showAdmobInterstitialAd() {
        Random rand = new Random();
        int n = rand.nextInt(2)+1;
        if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(getBaseContext()) && n == 1) {
            if (this.adInterstitialAd == null || !this.adInterstitialAd.isLoaded()) {
                admobCreateOrLoadInterstitial(this);
            } else {
                this.adInterstitialAd.show();
            }
        }
    }

    private void admobLoadIntestitialAd() {
        if (!this.adInterstitialAd.isLoading() && !this.adInterstitialAd.isLoaded()) {
            this.adInterstitialAd.loadAd(new Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        }
    }

    protected void facebookCreateOrLoadInterstitial(final Context context) {
        if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(context)) {
            Log.e("facebook","create done");
            this.fbInterstitialAd = new com.facebook.ads.InterstitialAd(this, Config.FACEBOOK_INTER_ADS_ID);
            this.fbInterstitialAd.setAdListener(new InterstitialAdListener() {
                public void onInterstitialDisplayed(Ad ad) {
                }

                public void onInterstitialDismissed(Ad ad) {
                }

                public void onError(Ad ad, AdError adError) {
//                    Toast.makeText(getApplicationContext(), "Error: " + adError.getErrorMessage(),
//                            Toast.LENGTH_LONG).show();
                }

                public void onAdLoaded(Ad ad) {
                }

                public void onAdClicked(Ad ad) {
                }

                public void onLoggingImpression(Ad ad) {
                }
            });
            facebookLoadInterstitialAd();
        }
    }

    private void facebookLoadInterstitialAd() {
        if (!this.fbInterstitialAd.isAdLoaded()) {
            this.fbInterstitialAd.loadAd();
        }
    }

    protected void showFacebookInterstitialAd() {
        Random rand = new Random();
        int n = rand.nextInt(2)+1;
        Log.e("facebook","random done "+ String.valueOf(n));
        if (!PrefUtil.getBooleanValue(this, Config.ITEM_SKU) && NetworkUtil.isOnline(getBaseContext()) && n == 1) {
            if (this.fbInterstitialAd == null || !this.fbInterstitialAd.isAdLoaded()) {
                facebookCreateOrLoadInterstitial(this);
            } else {
                this.fbInterstitialAd.show();
                Log.e("facebook","show done");
            }
        }
    }

    protected void onDestroy() {
        if (this.facebookView != null) {
            this.facebookView.destroy();
        }
        if (this.fbInterstitialAd != null) {
            this.fbInterstitialAd.destroy();
        }
        super.onDestroy();
    }
}
