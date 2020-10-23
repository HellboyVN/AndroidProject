package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.ads;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.base.BackBaseActivity;

public class NativeAdActivity extends BackBaseActivity {
    public static final int AD_START_POSITION = 0;
    public static final int ITEMS_PER_AD = 6;
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 150;
    private LinearLayout  adView;
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
//    private ImageView nativeAdIcon;
//    private TextView nativeAdTitle, nativeAdSocialContext, nativeAdBody;
//    private MediaView nativeAdMedia;
//    private Button nativeAdCallToAction;
//    private LinearLayout adChoicesContainer;
//    private AdChoicesView adChoicesView;
    private INativeAd item;
    protected void loadNativeExpressAd(final int index, final List<? extends INativeAd> dataList, final Context context) {
        if (index < dataList.size()) {
            item = (INativeAd) dataList.get(index);
            if (item.getAdView() != null) {
                nativeAd = new NativeAd(this, "333762600459223_333765303792286");

//                nativeAd.setAdListener(new AdListener() {
//                    public void onAdLoaded() {
//                        super.onAdLoaded();
//                        NativeAdActivity.this.loadNativeExpressAd(index + 6, dataList);
//                    }
//
//                    public void onAdFailedToLoad(int errorCode) {
//                        Log.e("I/Ads", "The previous Native Express ad failed to load. Attempting to load the next Native Express ad in the items list.");
//                        Log.e("I/Ads", String.valueOf(errorCode));
//                        NativeAdActivity.this.loadNativeExpressAd(index + 6, dataList);
//                    }
//                });
                nativeAd.setAdListener(new AdListener() {

                    @Override
                    public void onError(Ad ad, AdError error) {
                        Log.e("I/Ads", "The previous Native Express ad failed to load. Attempting to load the next Native Express ad in the items list.");
                        Log.e("I/Ads", String.valueOf(error));
                        NativeAdActivity.this.loadNativeExpressAd(index + 6, dataList,context);
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {

                        if (nativeAd != null) {
                            nativeAd.unregisterView();
                        }
                        // Create native UI using the ad metadata.
                        nativeAdContainer  = (LinearLayout) item.getAdView();
                        LayoutInflater inflater = LayoutInflater.from(context);
                        adView = (LinearLayout)   inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
                        nativeAdContainer.addView(adView);
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
                        LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
                        AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
                        adChoicesContainer.addView(adChoicesView);


                        // Register the Title and CTA button to listen for clicks.
                        List<View> clickableViews = new ArrayList<>();
                        clickableViews.add(nativeAdTitle);
                        clickableViews.add(nativeAdCallToAction);
                        nativeAd.registerViewForInteraction(nativeAdContainer,clickableViews);
//                        NativeAdActivity.this.loadNativeExpressAd(index + 6, dataList,context);
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

                // Request an ad
//                nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);
//                adView.loadAd(new Builder().build());
            }
        }
    }

    private int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public void setUpAndLoadNativeExpressAds(final int startPosition, RecyclerView recyclerView, final List<? extends INativeAd> levelDataList, final Context context) {
        recyclerView.post(new Runnable() {
            public void run() {
                float scale = NativeAdActivity.this.getResources().getDisplayMetrics().density;
                int i = 0;
                while (i < levelDataList.size()) {
                    if (i >= 0 && i <= levelDataList.size()) {
                        LinearLayout adView = (LinearLayout) ((INativeAd) levelDataList.get(i)).getAdView();
                        if (adView != null) {
                            CardView cardView = (CardView) NativeAdActivity.this.findViewById(R.id.adCardView);
                            int adWidth = NativeAdActivity.this.getScreenWidth() - (NativeAdActivity.this.getResources().getDimensionPixelSize(R.dimen.card_margin) * 4);
                            if (cardView != null) {
                                adWidth = (cardView.getWidth() - cardView.getPaddingLeft()) - cardView.getPaddingRight();
                            }
                        }
                    }
                    i += 6;
                }
                NativeAdActivity.this.loadNativeExpressAd(startPosition, levelDataList,context);
            }
        });
    }
}
