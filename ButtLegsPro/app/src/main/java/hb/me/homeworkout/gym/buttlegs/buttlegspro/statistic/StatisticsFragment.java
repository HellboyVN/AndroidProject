package hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventsListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter.SRecyclerAdapter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.lisener.IMetricChange;

public class StatisticsFragment extends Fragment implements EventsListener, IMetricChange {
    SRecyclerAdapter mAdapter;
    @BindView(R.id.statik_recycle)
    RecyclerView mRecyclerView;
    StatisticActivity parentActivity;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind((Object) this, view);
        EventCenter.getInstance().addIAPListener(this);
        EventCenter.getInstance().addMetricChangeListener(this);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
    }

    private void initList() {
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.parentActivity));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mAdapter = new SRecyclerAdapter(this.parentActivity);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.parentActivity = (StatisticActivity) context;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        EventCenter.getInstance().removeMetricChangeListener(this);
    }

    public void onDestroy() {
        super.onDestroy();
        EventCenter.getInstance().removeIAPListener(this);
    }

    public void updateRemoveAdsUI() {
    }

    public void setWaitScreen(boolean set) {
    }

    public void iabSetupFailed() {
    }

    public void notifyAdapterUpdate() {
    }

    public void onWeightMetricChange(int metric) {
        this.mAdapter.notifyDataSetChanged();
    }
}
