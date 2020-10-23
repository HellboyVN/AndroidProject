package hb.giphy.allgif.giffree.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.rey.material.widget.ProgressView;

import java.util.List;
import java.util.Random;

import hb.giphy.allgif.giffree.Admob;
import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.adapter.TenorRecycleAdapter;
import hb.giphy.allgif.giffree.model.GifItem;
import hb.giphy.allgif.giffree.presenter.RiffsyPresenter;
import hb.giphy.allgif.giffree.util.AppConstant;
import hb.giphy.allgif.giffree.util.LogUtil;
import hb.giphy.allgif.giffree.view.EndlessRecyclerViewScrollListener;


public class SearchViewActivity extends AppCompatActivity implements RiffsyPresenter.IPageView {

    LinearLayout mNoGifLayout;
    ProgressView mMoreProgressView;
    ProgressView mProgressView;
    RecyclerView mRecyclerView;
    private InterstitialAd interstitialAd;
    String mQuery;
    String mTag;

    private RiffsyPresenter mPresenter;
    private TenorRecycleAdapter mRecycleAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressView = (ProgressView) findViewById(R.id.search_loading_progressview);
        mMoreProgressView = (ProgressView) findViewById(R.id.search_more_progressview);
        mNoGifLayout = (LinearLayout) findViewById(R.id.linear_no_gif);
        initAdmob();
        handleIntent(getIntent());

    }

    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if ("android.intent.action.SEARCH".equals(intent.getAction())) {
            mQuery = intent.getStringExtra(SearchManager.QUERY);

        } else if (intent.hasExtra("tag")) {
            mQuery = intent.getStringExtra("tag");
        }
        getSupportActionBar().setTitle("#" + this.mQuery);
        mTag = this.mQuery.replace(" ", "+");
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mPresenter = new RiffsyPresenter(this, this, mQuery);
        loadRiffsy();
    }

    void loadRiffsy() {
        mPresenter.loadRiffy();
    }

    void getMoreData() {
        mPresenter.loadMoreRiffy();
    }

    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void showProgress() {
        mProgressView.start();
    }

    @Override
    public void hideProgress() {
        mProgressView.stop();

    }

    @Override
    public void onError(Exception e) {
        mProgressView.stop();
        Toast.makeText(this, R.string.network_error2, Toast.LENGTH_LONG).show();
        mNoGifLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChangeData(List<GifItem> data, boolean isFirst) {
        if (isFirst) {
            mRecycleAdapter = new TenorRecycleAdapter(this, data);
            mRecycleAdapter.setAdmob(admob);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mRecycleAdapter);
            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            gridLayoutManager.setGapStrategy(2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    getMoreData();
                }
            });
        } else {
            mMoreProgressView.stop();
            mRecycleAdapter.addItem(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        if (admob != null) {
            admob.destroy();
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
