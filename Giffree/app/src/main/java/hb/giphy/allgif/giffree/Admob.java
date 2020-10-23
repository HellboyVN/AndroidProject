package hb.giphy.allgif.giffree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import hb.giphy.allgif.giffree.util.AppConstant;
import hb.giphy.allgif.giffree.util.LogUtil;


/**
 * Created by nxtruong on 9/24/2017.
 */

public class Admob {

    public static final String TEST_DEVICE = "3F90C25D51474DDEA1C2B2319E1ADE19";
    private Context context;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private LinearLayout mNativeAdsLayout;
//    private NativeExpressAdView mNativeExpressAdView;


    public interface IAdListener {
        void onAdClosed();
    }

    public Admob(Context context) {
        super();
        this.context = context;
            mInterstitialAd = new InterstitialAd(context);
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.ad_interstitial_gif));
    }

    public void showInterstitialAd(final IAdListener adListener) {
        if(!AppConstant.isRemoveAds(context)) {
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE).build();
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (adListener != null) {
                        adListener.onAdClosed();
                    }
                }
            });

            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                if (adListener != null) {
                    adListener.onAdClosed();
                }
            }
        }
    }


    public void adBanner(ViewGroup viewGroup) {
        if(!AppConstant.isRemoveAds(context)) {
            mAdView = new AdView(context);
            if (!AppConstant.isRemoveAds(context)) {
                mAdView.setAdSize(AdSize.BANNER);
                mAdView.setAdUnitId(context.getResources().getString(R.string.ad_banner_gif));
                viewGroup.addView(mAdView);

                AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE).build();
                mAdView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                        LogUtil.d("Load error ... " + errorCode);
                        mAdView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        LogUtil.d("Load successful ... ");
                        mAdView.setVisibility(View.VISIBLE);
                    }
                });
                mAdView.loadAd(adRequest);
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
