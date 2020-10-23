package hb.me.homeworkout.gym.buttlegs.buttlegspro.iab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.base.BackBaseActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.IabHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.IabHelper.OnConsumeFinishedListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.IabHelper.OnIabPurchaseFinishedListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.IabHelper.OnIabSetupFinishedListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.IabHelper.QueryInventoryFinishedListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.IabResult;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.Inventory;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.iap_utils.Purchase;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;

public abstract class InAppActivity extends BackBaseActivity {
    static final int RC_REQUEST = 10001;
//    public static final String SKU_REMOVE_ADS = "android.test.purchased";
    private InterstitialAd interstitialAd;
    private  AdView adsViewFace;
    public static final String SKU_REMOVE_ADS = "caotramanh";
    static final String TAG = "InAppActivity";
    OnConsumeFinishedListener mConsumeFinishedListener = new OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(InAppActivity.TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            if (InAppActivity.this.mHelper != null) {
                if (result.isSuccess()) {
                    Log.d(InAppActivity.TAG, "Consumption successful. Provisioning.");
                    InAppActivity.this.mIsRemoveAdsAvailable = true;
                    InAppActivity.this.saveData();
                    Toast.makeText(getApplicationContext(),"Please restart app for taking effect!",Toast.LENGTH_LONG).show();
                } else {
                    InAppActivity.this.complain("Error while consuming: " + result);
                }
                InAppActivity.this.updateRemoveAdsUI();
                InAppActivity.this.setWaitScreen(false);
                Log.d(InAppActivity.TAG, "End consumption flow.");
            }
        }
    };
    QueryInventoryFinishedListener mGotInventoryListener = new QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(InAppActivity.TAG, "Query inventory finished.");
            if (InAppActivity.this.mHelper != null) {
                if (result.isFailure()) {
                    InAppActivity.this.complain("Failed to query inventory: " + result);
                } else if (!InAppActivity.this.mIsRemoveAdsAvailable) {
                    Purchase removeAdsPurchase = inventory.getPurchase(InAppActivity.SKU_REMOVE_ADS);
                    if (removeAdsPurchase != null && InAppActivity.this.verifyDeveloperPayload(removeAdsPurchase)) {
                        InAppActivity.this.mIsRemoveAdsAvailable = true;
                    }
                    InAppActivity.this.updateRemoveAdsUI();
                    InAppActivity.this.setWaitScreen(false);
                    Log.d(InAppActivity.TAG, "Initial inventory query finished; enabling main UI.");
                }
            }
        }
    };
    IabHelper mHelper;
    private boolean mIsIabSetupSuccess = false;
    private boolean mIsRemoveAdsAvailable = false;
    OnIabPurchaseFinishedListener mPurchaseFinishedListener = new OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(InAppActivity.TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (InAppActivity.this.mHelper != null) {
                if (result.isFailure()) {
                    if (result.getResponse() != IabHelper.IABHELPER_USER_CANCELLED) {
                        InAppActivity.this.complain("Error purchasing: " + result);
                    }
                    InAppActivity.this.setWaitScreen(false);
                } else if (InAppActivity.this.verifyDeveloperPayload(purchase)) {
                    Log.d(InAppActivity.TAG, "Purchase successful.");
                    if (purchase.getSku().equals(InAppActivity.SKU_REMOVE_ADS)) {
                        Log.d(InAppActivity.TAG, "Purchase is custom workout. Starting displaying it.");
                        InAppActivity.this.mIsRemoveAdsAvailable = true;
                        //hellboy
                        try {
                            InAppActivity.this.mHelper.consumeAsync(purchase, InAppActivity.this.mConsumeFinishedListener);
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            e.printStackTrace();
                        }
                        InAppActivity.this.updateRemoveAdsUI();
                        InAppActivity.this.setWaitScreen(false);
                    }
                } else {
                    InAppActivity.this.complain("Error purchasing. Authenticity verification failed.");
                    InAppActivity.this.setWaitScreen(false);
                }
            }
        }
    };
    protected void faceBookBanner(final Context context, final RelativeLayout relativeLayout){
        MobileAds.initialize(this, ConsKeys.ADMOB_APP_ID);
        if (!mIsRemoveAdsAvailable) {
            adsViewFace = new com.facebook.ads.AdView(context, "141466806566650_141467596566571", com.facebook.ads.AdSize.BANNER_320_50);
            relativeLayout.addView(adsViewFace);
            AdSettings.addTestDevice("fdb6f62dec4e0f47749767900b77268b");
            adsViewFace.loadAd();
        }
    }
    protected void admobBanner(com.google.android.gms.ads.AdView adView) {
        try {
            if (!mIsRemoveAdsAvailable) {
                Log.e("levan ads----","banner");
//                  adView.setAdSize(AdSize.BANNER);
//                adView.setAdUnitId(ConsKeys.BANNER_BEGIN_WORKOUT);
                adView.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
            }
        } catch (Exception e) {
        }
    }
    protected void showInterFacebook(Context context){
        if(!mIsRemoveAdsAvailable){
            interstitialAd = new InterstitialAd(context, "141466806566650_141467579899906");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
            interstitialAd.loadAd();
        }
    }
    protected abstract void iabSetupFailed();

    protected abstract void setWaitScreen(boolean z);

    protected abstract void updateRemoveAdsUI();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPurchaseData();
        initPurchase();
    }

    public void initPurchase() {
        this.mHelper = new IabHelper(this, getResources().getString(R.string.in_app_key));
        this.mHelper.enableDebugLogging(false);
        this.mHelper.startSetup(new OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(InAppActivity.TAG, "Setup finished.");
                InAppActivity.this.mIsIabSetupSuccess = result.isSuccess();
                if (!result.isSuccess()) {
                    Log.e(InAppActivity.TAG, "Problem setting up in-app billing: " + result);
                } else if (InAppActivity.this.mHelper != null) {
                    Log.d(InAppActivity.TAG, "Setup successful. Querying inventory.");
                    try {
                        InAppActivity.this.mHelper.queryInventoryAsync(InAppActivity.this.mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mHelper != null && !this.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onDestroy() {
        if(this.adsViewFace !=null) {
            this.adsViewFace.destroy();
        }
        super.onDestroy();
        Log.d(TAG, "Destroying helper.");
        if (this.mHelper != null && this.mIsIabSetupSuccess) {
            try {
                this.mHelper.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.mHelper = null;
        }
    }

    public boolean isRemoveAdsPurchased() {
        return this.mIsRemoveAdsAvailable;
    }

    public void onPurchaseClick() {
        if (!this.mIsRemoveAdsAvailable) {
            setWaitScreen(true);
            Log.d(TAG, "Launching purchase flow for remove ads.");
            String payload = "";
            if (this.mHelper != null) {
                try {
                    this.mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, RC_REQUEST, this.mPurchaseFinishedListener, payload);
                } catch (IllegalStateException e) {
                    Toast.makeText(this, getString(R.string.in_app_retry), Toast.LENGTH_SHORT).show();
                    this.mHelper.flagEndAsync();
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    void complain(String message) {
        alert("Error: " + message);
    }

    void alert(String message) {
        android.support.v7.app.AlertDialog.Builder bld = new  android.support.v7.app.AlertDialog.Builder(this);
        bld.setMessage((CharSequence) message);
        bld.setNeutralButton((CharSequence) "OK", null);
        bld.create().show();
    }

    void saveData() {
        SharedPrefsService.getInstance().setPurchasedRemoveAds(getApplicationContext(), true);
    }

    protected void loadPurchaseData() {
        this.mIsRemoveAdsAvailable = SharedPrefsService.getInstance().getPurchasedRemoveAds(getApplicationContext());
    }
}
