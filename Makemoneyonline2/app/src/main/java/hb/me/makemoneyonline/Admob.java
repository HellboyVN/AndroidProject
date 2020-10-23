package hb.me.makemoneyonline;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by nxtruong on 6/24/2017.
 */

public class Admob {

    public static final String TEST_DEVICE = "3F90C25D51474DDEA1C2B2319E1ADE19";
    private Context context;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;


    private LinearLayout mNativeAdsLayout;

    public interface IAdListener {
        void onAdClosed();
    }

    public Admob(Context context) {
        super();
        this.context = context;
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.mmo_app_full_id));
    }

    public void showInterstitialAd(final IAdListener adListener) {

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


    public void adBanner(ViewGroup viewGroup) {

        mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(context.getResources().getString(R.string.mmo_app_banner_id));
        viewGroup.addView(mAdView);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE).build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                mAdView.setVisibility(View.GONE);
                LogUtils.d("Load failure...");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
                LogUtils.d( "Load success...");
            }
        });
        mAdView.loadAd(adRequest);
    }


    public void pause() {
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    public void resume() {
        if (mAdView != null) {
            mAdView.resume();
        }

    }

    public void destroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

}
