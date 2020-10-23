package hb.giphy.allgif.giffree.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.Random;

import hb.giphy.allgif.giffree.Admob;
import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.fragment.CategoryFragment;
import hb.giphy.allgif.giffree.util.AppConstant;
import hb.giphy.allgif.giffree.util.LogUtil;


public class CategoryActivity extends AppCompatActivity {
    private InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        String categoryName = getIntent().getStringExtra(AppConstant.BUNDLE_CATEGORY_NAME);
        getSupportActionBar().setTitle(categoryName);
        int categoryId = getCategoryIdByName(categoryName);
        LogUtil.d("Select category ID: " + categoryId);
        initAdmob();
        Bundle bundle = new Bundle();
        bundle.putInt(AppConstant.BUNDLE_CATEGORY_ID, categoryId);
        Fragment fragment = new CategoryFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.category_act, fragment).commit();


    }

    int getCategoryIdByName(String name) {
        String[] listSections = getResources().getStringArray(R.array.listExplore);
        if (listSections != null && listSections.length > 0) {
            for (int i = 0; i < listSections.length; i++) {
                if (listSections[i].equals(name)) {
                    return i + 1;
                }
            }
        }
        return 1;
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }


    Admob admob;

    void initAdmob() {
        admob = new Admob(this);
        admob.showInterstitialAd(null);
        admob.adBanner((LinearLayout) findViewById(R.id.ad_linear));
        showFAd();
    }
    void showFAd() {
        if(!AppConstant.isRemoveAds(getApplicationContext())) {
            interstitialAd = new InterstitialAd(this, "169523040462460_169525823795515");
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
                    LogUtil.d("Error: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Show the ad when it's done loading.
                    Random rand = new Random();
                    int n = rand.nextInt(3) + 1;
                    Log.e("levan_new", "show + n= " + String.valueOf(n));
                    if (n == 1) {
                        interstitialAd.show();
                    }
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
    @Override
    public void onResume() {
        super.onResume();
        if (admob != null) {
            admob.pause();
        }
        LogUtil.d( "onResume() PageFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (admob != null) {
            admob.resume();
        }
        LogUtil.d("onPAUSE() PageFragment");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (admob != null) {
            admob.destroy();
        }
    }
}
