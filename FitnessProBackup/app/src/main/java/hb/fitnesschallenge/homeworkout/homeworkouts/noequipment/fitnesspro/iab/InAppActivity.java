package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.iab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog.Builder;
import android.util.Log;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.FabricLogger;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.NativeAdActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.base.BackBaseActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;

public abstract class InAppActivity extends NativeAdActivity implements IBillingHandler {
    static final int RC_REQUEST = 10001;
//    public static final String SKU_PRO_PACKAGE = "android.test.purchased";
    public static final String SKU_PRO_PACKAGE = "truonglevan";
    static final String TAG = "InAppActivity";
    private InterstitialAd interstitialAd;
    BillingProcessor bp;
//    public static final String SKU_REMOVE_ADS = "android.test.purchased";
    boolean mIsProPackageAvailable = false;

    protected abstract void setWaitScreen(boolean z);

    protected abstract void updateRemoveAdsUI();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPurchaseData();
        initPurchase();
    }

    public void initPurchase() {
        this.bp = new BillingProcessor(this, getResources().getString(R.string.in_app_key), this);
        if (!this.bp.isInitialized()) {
            this.bp.initialize();
        }
    }

    public void onBillingInitialized() {
        if (this.bp.isInitialized()) {
            if (this.bp.loadOwnedPurchasesFromGoogle()) {
                boolean updateUI = !this.mIsProPackageAvailable;
                if (this.bp.isPurchased(SKU_PRO_PACKAGE)) {
                    this.mIsProPackageAvailable = true;
                    saveProPackagePurchaseData();
                }
                if (updateUI) {
                    updateRemoveAdsUI();
                    setWaitScreen(false);
                    Log.d(TAG, "Initial inventory query finished; enabling main UI.");
                }
            }
            SharedPrefsService.getInstance().setPurchaseAvailable(this, this.bp.isOneTimePurchaseSupported());
            return;
        }
        SharedPrefsService.getInstance().setPurchaseAvailable(this, true);
    }

    public void onProductPurchased(String productId, TransactionDetails details) {
        Log.d(TAG, "Purchase successful.");
        if (productId.equals(SKU_PRO_PACKAGE)) {
            Log.d(TAG, "Purchase is pro package. Starting displaying it.");
            this.mIsProPackageAvailable = true;
            Toast.makeText(getApplicationContext(),"Purchase succesfully, please restart app to see the effect",Toast.LENGTH_LONG).show();
            saveProPackagePurchaseData();
            updateRemoveAdsUI();
            setWaitScreen(false);
            FabricLogger.getInstance().logPurchase(true);
        }
    }

    public void onBillingError(int errorCode, Throwable error) {
        if (errorCode == 7) {
            this.mIsProPackageAvailable = true;
            saveProPackagePurchaseData();
            updateRemoveAdsUI();
            setWaitScreen(false);
        }
        if (error != null) {
            complain("Error purchasing: " + error.getMessage());
        }
        setWaitScreen(false);
    }

    public void onPurchaseHistoryRestored() {
        boolean updateUI;
        Log.d(TAG, "Query inventory finished.");
        if (this.mIsProPackageAvailable) {
            updateUI = false;
        } else {
            updateUI = true;
        }
        if (this.bp.isPurchased(SKU_PRO_PACKAGE)) {
            this.mIsProPackageAvailable = true;

            saveProPackagePurchaseData();
        }
        if (updateUI) {
            updateRemoveAdsUI();
            setWaitScreen(false);
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == BackBaseActivity.REQUEST_LEVEL_PREVIEW) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (!this.bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isProPackagePurchased() {
        return this.mIsProPackageAvailable;
    }

    public void onPurchaseClick() {
        if (!this.bp.isInitialized() || (this.bp.isInitialized() && !this.bp.isOneTimePurchaseSupported())) {
            complain(getResources().getString(R.string.issue_with_purchase));
        } else if (!this.mIsProPackageAvailable) {
            setWaitScreen(true);
            this.bp.purchase(this, SKU_PRO_PACKAGE);
        }
    }

    void complain(String message) {
        alert("Error: " + message);
    }

    void alert(String message) {
        Builder bld = new Builder(this);
        bld.setMessage((CharSequence) message);
        bld.setNeutralButton((CharSequence) "OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void saveProPackagePurchaseData() {
        SharedPrefsService.getInstance().setPurchasedProPackage(getApplicationContext(), true);
    }

    protected void loadPurchaseData() {
        this.mIsProPackageAvailable = SharedPrefsService.getInstance().getPurchasedProPackage(getApplicationContext());
    }

    public void onDestroy() {
        if (this.bp != null) {
            this.bp.release();
        }
        super.onDestroy();
    }
    protected void showInterFacebook(Context context){
        Log.e("hellboy","facebook ads full");
        if(!this.mIsProPackageAvailable){
            interstitialAd = new InterstitialAd(context, "2357344674291719_2357345024291684");
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
}
