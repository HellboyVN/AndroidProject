package hb.me.giphy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;

import java.util.List;

import hb.me.giphy.Admob;
import hb.me.giphy.R;
import hb.me.giphy.adapter.TenorRecycleAdapter;
import hb.me.giphy.model.GifItem;
import hb.me.giphy.presenter.RiffsyPresenter;
import hb.me.giphy.util.AppRater;
import hb.me.giphy.util.LogUtil;
import hb.me.giphy.view.EndlessRecyclerViewScrollListener;


public class TrendingFragment extends Fragment implements RiffsyPresenter.IPageView {

    private LinearLayout mNoGifLayout;
    private ProgressView mProgressBarMore;
    private ProgressView mProgressBarLoading;
    private RecyclerView mRecyclerView;

    private RiffsyPresenter mPresenter;
    private TenorRecycleAdapter mRecycleAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d("Load HOME Fragment");
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.all_fragment, container, false);
        try {
            AppRater.app_launched(getActivity());
            this.mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
            this.mProgressBarLoading = (ProgressView) root.findViewById(R.id.trends_loading_progressview);
            this.mProgressBarMore = (ProgressView) root.findViewById(R.id.trends_more_progressview);
            this.mNoGifLayout = (LinearLayout) root.findViewById(R.id.linear_no_gif);

            mPresenter = new RiffsyPresenter(this, this.getActivity());
            loadRiffsy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initAdmob();
        return root;
    }



    void loadRiffsy() {

        mPresenter.loadTrending();
    }

    void getMoreData() {
        mPresenter.loadMoreTrending();
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
        Toast.makeText(getActivity(), R.string.network_error2, Toast.LENGTH_LONG).show();
        mNoGifLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onChangeData(List<GifItem> data, boolean isFirst) {
        if (isFirst) {
            mRecycleAdapter = new TenorRecycleAdapter(getActivity(), data);
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
            mProgressBarMore.stop();
            mRecycleAdapter.addItem(data);
        }
    }

    Admob admob;

    void initAdmob() {
        admob = new Admob(getContext());
        admob.showInterstitialAd(null);
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
    public void onDestroy() {
        super.onDestroy();
        if (admob != null) {
            admob.destroy();
        }
    }
}
