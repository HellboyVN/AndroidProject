package hb.homeworkout.homeworkouts.noequipment.fitnesspro.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;

public class MoreFragment extends BaseFragment implements BillingProcessor.IBillingHandler {


    public static MoreFragment newInstance() {
        return new MoreFragment();
    }
    Button btn_moreApp,btn_removeAds;
    BillingProcessor bp;
    private LinearLayout adsView;
    private LinearLayout adView;
    private NativeAd nativeAd;
    private String ggpublishkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuUhr/25XhrrJ9uOFn8wv30sB03nTEmGOSokktwfoOs8xHjOeWnPtF4M7kIirtO0gLdaL2Y/RYkZf+Nf+9FAcKAQEm7CPKoObBCK8qZZFeDQMcscjzdWO7Dz/lqmvs0dxt/JOy58e6ukbpMLb6vxI62J99dHgtDZ3MgJzy1a/6T378omaI6ZTuLcC2vabZVDvnVxpX1xfchwU3Rd+CxBeWMxqTbczG3n7GpyhlvgHiVM83UXI501XPR+ABBN5iq/eTzObq/PTU0TTrO8LlfzcRevpBXwiHsk26FIsb7Y7AVsV8lJrG9WvVHAWcIWyP3K4rGvRTEmYjcKajqx79gUYCQIDAQAB";
    private LinearLayout  nativeAdContainer;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        btn_moreApp = (Button)view.findViewById(R.id.buttonMoreApp);
        btn_removeAds = (Button)view.findViewById(R.id.buttonRemoveAds);
        this.adsView = (LinearLayout) view.findViewById(R.id.adsView);
        bp = new BillingProcessor(getActivity(), ggpublishkey, this);
        btn_moreApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("check sharepreferent ",String.valueOf(PersistenceManager.isAdsFreeVersion()));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=hb.me.homeworkout.gym.buttlegs.buttlegspro"));
                getActivity().startActivity(intent);
            }
        });
        btn_removeAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePremium();
            }
        });

        showNativeAd(view);
        return view;
    }


    private boolean handlePremium() {
        if (PersistenceManager.isAdsFreeVersion()) {
            Toast.makeText(getActivity(), "Ads are already removed!", Toast.LENGTH_SHORT).show();
            return true;
        }else{
            bp.purchase(getActivity(), "caotramanh");//caotramanh
            return true;
        }
    }
    private void showNativeAd(final View view) {
        if (!PersistenceManager.isAdsFreeVersion()) {
            Log.e("NativeADS", "----HERE----");
            nativeAd = new NativeAd(getActivity(), "333762600459223_333765303792286");
            nativeAd.setAdListener(new AdListener() {

                @Override
                public void onError(Ad ad, AdError error) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.e("NativeADSLoaded", "----HERE----");
                    adsView.setVisibility(View.VISIBLE);
                    if (nativeAd != null) {
                        nativeAd.unregisterView();
                    }

                    // Add the Ad view into the ad container.
                    nativeAdContainer = (LinearLayout) view.findViewById(R.id.adsView);
                    LayoutInflater inflater = LayoutInflater.from(getActivity());
                    // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                    adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
                    nativeAdContainer.addView(adView);

                    // Create native UI using the ad metadata.
                    ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                    TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                    MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                    TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                    TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                    Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                    // Set the Text.
                    nativeAdTitle.setText(nativeAd.getAdTitle());
                    nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                    nativeAdBody.setText(nativeAd.getAdBody());
                    nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                    // Download and display the ad icon.
                    NativeAd.Image adIcon = nativeAd.getAdIcon();
                    NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                    // Download and display the cover image.
                    nativeAdMedia.setNativeAd(nativeAd);

                    // Add the AdChoices icon
                    LinearLayout adChoicesContainer = (LinearLayout) view.findViewById(R.id.ad_choices_container);
                    AdChoicesView adChoicesView = new AdChoicesView(getActivity(), nativeAd, true);
                    adChoicesContainer.addView(adChoicesView);

                    // Register the Title and CTA button to listen for clicks.
                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdCallToAction);
                    nativeAd.registerViewForInteraction(nativeAdContainer, clickableViews);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });

            nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);
        }
    }




    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(getActivity(), "All ads removed!", Toast.LENGTH_LONG).show();
        PersistenceManager.setAdsFreeVersion(true);
        Log.e("check sharepreferent ",String.valueOf(PersistenceManager.isAdsFreeVersion()));
        Toast.makeText(getActivity(), "Please Restart this app for taking effect!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(getActivity(), "Your Purchase has been canceled!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            MoreFragment.this.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
