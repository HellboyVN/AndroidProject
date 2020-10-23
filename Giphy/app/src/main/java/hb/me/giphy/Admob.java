package hb.me.giphy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import hb.me.giphy.util.AppConstant;
import hb.me.giphy.util.LogUtil;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by nxtruong on 9/24/2017.
 */

public class Admob {

    public static final String TEST_DEVICE = "3F90C25D51474DDEA1C2B2319E1ADE19";
    private Context context;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;
    private com.facebook.ads.AdView adViewface;
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
        if(!AppConstant.isRemoveAds(getApplicationContext())) {
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

    public void facebookBaner(LinearLayout linearLayout){
        if(!AppConstant.isRemoveAds(getApplicationContext())) {
            adViewface = new com.facebook.ads.AdView(context, "126646304687890_141398803212640", com.facebook.ads.AdSize.BANNER_320_50);
            linearLayout.addView(adViewface);
            adViewface.loadAd();
        }
    }

    public void adBanner(ViewGroup viewGroup) {

        mAdView = new AdView(context);
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
        if(adViewface !=null){
            adViewface.destroy();
        }
//        if (mNativeExpressAdView != null) {
//            mNativeExpressAdView.destroy();
//        }
    }

//    public void showNativeAd(final ViewGroup nativeAdContainer) {
//            this.mNativeAdsLayout = (LinearLayout) LayoutInflater.from(this.context).inflate(R.layout.native_admob_ads, nativeAdContainer, false);
//            this.mNativeExpressAdView = (NativeExpressAdView) this.mNativeAdsLayout.findViewById(R.id.adViewNative);
//            nativeAdContainer.addView(this.mNativeAdsLayout);
//            this.mNativeExpressAdView.setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build());
//            this.mNativeExpressAdView.setAdListener(new AdListener() {
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                    nativeAdContainer.setVisibility(View.VISIBLE);
//                    mNativeAdsLayout.setPadding(0, 15, 0, 0);
//                }
//
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//                    nativeAdContainer.removeView(mNativeAdsLayout);
//                }
//            });
//            this.mNativeExpressAdView.loadAd(new AdRequest.Builder().addTestDevice(TEST_DEVICE).build());
//    }

}
