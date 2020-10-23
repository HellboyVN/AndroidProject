package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import java.util.ArrayList;
import java.util.List;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.listener.IAdCardClicked;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.database.RealmManager;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.MainActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.BaseFragment;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.listener.MIAPListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.model.TabTwoData;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners.EventCenter;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RecyclerItemClickListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RecyclerItemClickListener.OnItemClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreFragment extends BaseFragment implements IWeekChange, MIAPListener, IAdCardClicked {
    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";
    MoreTabRecycelerAdapter adapter;
    List<TabTwoData> dataList;
    private View headerView;
    MainActivity parentActivity;
    private Typeface rBold;
    private Typeface rLight;
    @BindView(R.id.scrollable)
    ObservableRecyclerView recyclerView;
    private int weekOffset = 0;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventCenter.getInstance().addIAPListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind((Object) this, view);
        this.headerView = LayoutInflater.from(getActivity()).inflate(R.layout.padding, null);
        setupMoreTabView();
        if (this.parentActivity instanceof ObservableScrollViewCallbacks) {
            Bundle args = getArguments();
            if (args != null && args.containsKey("ARG_INITIAL_POSITION")) {
                final int initialPosition = args.getInt("ARG_INITIAL_POSITION", 0);
                ScrollUtils.addOnGlobalLayoutListener(this.recyclerView, new Runnable() {
                    public void run() {
                        MoreFragment.this.recyclerView.scrollVerticallyToPosition(initialPosition);
                    }
                });
            }
            this.recyclerView.setTouchInterceptionViewGroup((ViewGroup) this.parentActivity.findViewById(R.id.root));
            this.recyclerView.setScrollViewCallbacks(this.parentActivity);
        }
        return view;
    }

    private void setupMoreTabView() {
        this.dataList = new ArrayList();
        TabTwoData label = new TabTwoData();
        label.setViewType(5);
        TabTwoData chartWeek = new TabTwoData();
        chartWeek.setViewType(-1);
        int index = 0 + 1;
        this.dataList.add(0, label);
        int index2 = index + 1;
        this.dataList.add(index, chartWeek);
        if (RealmManager.getInstance().hasExerciseData(this.parentActivity)) {
            TabTwoData labelAllPlanks = new TabTwoData();
            labelAllPlanks.setViewType(5);
            labelAllPlanks.setName(getString(R.string.type_stats_label));
            index = index2 + 1;
            this.dataList.add(index2, labelAllPlanks);
            TabTwoData exTimeCard = new TabTwoData();
            exTimeCard.setViewType(6);
            exTimeCard.setPurchased(true);
            index2 = index + 1;
            this.dataList.add(index, exTimeCard);
        }
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.parentActivity));
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.adapter = new MoreTabRecycelerAdapter(this.parentActivity, this.dataList, this.headerView, this, this);
        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.parentActivity, this.recyclerView, new OnItemClickListener() {
            public void onItemClick(View view, int position) {
            }

            public void onItemLongClick(View view, int position) {
            }
        }));
        updateCharts();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.parentActivity = (MainActivity) context;
        }
    }

    public void updateCharts() {
        this.adapter.notifyDataSetChanged();
    }

    public void onWeekChange(int offset) {
        this.weekOffset += offset;
        if (this.weekOffset > 0) {
            this.weekOffset = 0;
        }
        for (TabTwoData tabTwoData : this.dataList) {
            tabTwoData.setWeekOffset(this.weekOffset);
        }
        this.adapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        super.onDestroy();
        EventCenter.getInstance().removeIAPListener(this);
    }

    public void removeAdClicked(int position) {
        this.parentActivity.onPurchaseClick();
    }

    public void whySeeAd() {
    }

    public void updateRemoveAdsUI() {
        TabTwoData elem = TabTwoData.getItemByType(this.dataList, 6);
        if (elem != null) {
            elem.setPurchased(this.parentActivity.isProPackagePurchased());
            this.adapter.notifyDataSetChanged();
        }
    }

    public void setWaitScreen(boolean set) {
    }

    public void iabSetupFailed() {
    }
}
