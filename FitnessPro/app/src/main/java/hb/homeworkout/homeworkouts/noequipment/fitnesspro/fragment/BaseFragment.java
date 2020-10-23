package hb.homeworkout.homeworkouts.noequipment.fitnesspro.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

public class BaseFragment extends Fragment {
    private AdView adView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) {
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public void onResume() {
        super.onResume();
        if (this.adView != null) {
            this.adView.resume();
        }
        Log.w("BASE_FRAGMENT", " onResume");
    }

    public void onPause() {
        if (this.adView != null) {
            this.adView.pause();
        }
        Log.w("BASE_FRAGMENT", " onPause");
        super.onPause();
    }

    public void loadBannerAdvertisement(View view) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            String adId = "ca-app-pub-8468661407843417/4471893404";
            if (true) {
                this.adView = new AdView(getActivity());
                this.adView.setAdSize(AdSize.SMART_BANNER);
                this.adView.setAdUnitId(adId);
                ((LinearLayout) view.findViewById(R.id.adView)).addView(this.adView);
                AdRequest adRequest = AppUtil.getAdRequest();
                if (this.adView != null) {
                    this.adView.setVisibility(View.GONE);
                    this.adView.loadAd(adRequest);
                }
                if (this.adView != null) {
                    this.adView.setAdListener(new AdListener() {
                        public void onAdLoaded() {
                            BaseFragment.this.adView.setVisibility(View.VISIBLE);
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
    }
}
