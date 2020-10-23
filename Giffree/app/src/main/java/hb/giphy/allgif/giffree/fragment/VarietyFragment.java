package hb.giphy.allgif.giffree.fragment;

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

import hb.giphy.allgif.giffree.Admob;
import hb.giphy.allgif.giffree.R;
import hb.giphy.allgif.giffree.adapter.TenorRecycleAdapter;
import hb.giphy.allgif.giffree.model.GifItem;
import hb.giphy.allgif.giffree.presenter.RiffsyPresenter;
import hb.giphy.allgif.giffree.util.LogUtil;
import hb.giphy.allgif.giffree.view.EndlessRecyclerViewScrollListener;


public class VarietyFragment extends Fragment implements RiffsyPresenter.IPageView {
    private ProgressView mProgressBarMore;
    private ProgressView mProgressBarLoading;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoGifLayout;

    private RiffsyPresenter mPresenter;
    private TenorRecycleAdapter mRecycleAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.variety_fragment, container, false);
        mPresenter = new RiffsyPresenter(this, getActivity(), "gif");
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mProgressBarLoading = (ProgressView) root.findViewById(R.id.variety_loading_progress_view);
        mNoGifLayout = (LinearLayout) root.findViewById(R.id.linear_no_gif);
        mProgressBarMore = (ProgressView) root.findViewById(R.id.variety_more_progress_view);
        loadRiffsy();
        initAdmob();
        return root;
    }


    void loadRiffsy() {
        mPresenter.loadRiffy();
    }

    void getMoreData() {
        mPresenter.loadMoreRiffy();
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
