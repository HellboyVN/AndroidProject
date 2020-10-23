package lp.me.allgifs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class DetailCategoryActivity extends AppCompatActivity implements RiffsyPresenter.IPageView {

    private LinearLayout mNoGifLayout;
    private ProgressView mProgressBarMore;
    private ProgressView mProgressBarLoading;
    private RecyclerView mRecyclerView;

    private String mQuery = "";

    private RiffsyPresenter mPresenter;
    private TenorRecycleAdapter mRecycleAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_category);
        mQuery = getIntent().getExtras().getString("type_key");
        mPresenter = new RiffsyPresenter(this, this, mQuery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(mQuery);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBarLoading = (ProgressView) findViewById(R.id.search_loading_progressview);
        mProgressBarMore = (ProgressView) findViewById(R.id.search_more_progressview);
        mNoGifLayout = (LinearLayout) findViewById(R.id.linear_no_gif);
        initAdmob();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != android.R.id.home) {
            return super.onOptionsItemSelected(item);
        }
        finish();
        return true;
    }

    @Override
    public void showProgress() {
        mProgressBarLoading.start();
    }

    @Override
    public void hideProgress() {
        mProgressBarLoading.stop();
    }

    @Override
    public void onError(Exception e) {
        mProgressBarLoading.stop();
        Toast.makeText(this, R.string.network_error2, Toast.LENGTH_LONG).show();
        mNoGifLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChangeData(List<GifItem> data, boolean isFirst) {
        if (isFirst) {
            mRecycleAdapter = new TenorRecycleAdapter(this, data);
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
            mProgressBarMore.stop();
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

}
