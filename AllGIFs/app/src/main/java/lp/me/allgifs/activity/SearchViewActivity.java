package lp.me.allgifs.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.List;

import lp.me.allgifs.Admob;
import lp.me.allgifs.R;
import lp.me.allgifs.adapter.TenorRecycleAdapter;
import lp.me.allgifs.model.GifItem;
import lp.me.allgifs.presenter.RiffsyPresenter;
import lp.me.allgifs.util.LogUtil;
import lp.me.allgifs.view.EndlessRecyclerViewScrollListener;


public class SearchViewActivity extends AppCompatActivity implements RiffsyPresenter.IPageView {

    LinearLayout mNoGifLayout;
    ProgressView mMoreProgressView;
    ProgressView mProgressView;
    RecyclerView mRecyclerView;

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
