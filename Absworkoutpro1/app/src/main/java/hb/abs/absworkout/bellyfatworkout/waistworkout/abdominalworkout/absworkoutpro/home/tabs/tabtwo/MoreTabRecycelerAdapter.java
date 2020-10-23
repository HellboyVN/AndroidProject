package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.ads.listener.IAdCardClicked;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards.ChartWeekCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards.HeaderCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards.LabelCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards.MoreTabAbstractCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.model.TabTwoData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.othercards.ExerciseTimeCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.othercards.PollCard;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class MoreTabRecycelerAdapter extends Adapter<ViewHolder> {
    public static final int VIEW_TYPE_CHART_DAY = 3;
    public static final int VIEW_TYPE_CHART_WEEK = -1;
    public static final int VIEW_TYPE_EXERCISE_TIME = 6;
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_LABEL = 5;
    public static final int VIEW_TYPE_POLL = 4;
    private List<TabTwoData> dataList;
    private IAdCardClicked mAdListener;
    private Context mContext;
    private View mHeaderView;
    private IWeekChange mListener;

    public MoreTabRecycelerAdapter(Context mContext, List<TabTwoData> dataList, View headerView, IWeekChange listener, IAdCardClicked iAdCardClicked) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.mHeaderView = headerView;
        this.mListener = listener;
        this.mAdListener = iAdCardClicked;
    }

    public int getItemViewType(int position) {
        int rPosition;
        if (position > 0) {
            rPosition = position - 1;
        } else {
            rPosition = position;
        }
        switch (((TabTwoData) this.dataList.get(rPosition)).getViewType()) {
            case -1:
                if (position != 0) {
                    return -1;
                }
                return 0;
            case 4:
                if (position != 0) {
                    return 4;
                }
                return 0;
            case 5:
                return position == 0 ? 0 : 5;
            case 6:
                if (position != 0) {
                    return 6;
                }
                return 0;
            default:
                if (position == 0) {
                    return 0;
                }
                return 3;
        }
    }

    public int getItemCount() {
        if (this.mHeaderView == null) {
            return this.dataList.size();
        }
        return this.dataList.size() + 1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MoreTabAbstractCard card;
        switch (viewType) {
            case -1:
                return new ChartWeekCard(this.mContext, parent);
            case 4:
                return new PollCard(this.mContext, parent);
            case 5:
                card = new LabelCard(this.mContext, parent);
                ((LabelCard) card).setWeekChangeListener(this.mListener);
                return card;
            case 6:
                card = new ExerciseTimeCard(this.mContext, parent);
                ((ExerciseTimeCard) card).setAdClickListener(this.mAdListener);
                return card;
            default:
                return new HeaderCard(this.mContext, this.mHeaderView);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int realPosition = 0;
        if (position > 0) {
            realPosition = position - 1;
        }
        ((MoreTabAbstractCard) viewHolder).bind((TabTwoData) this.dataList.get(realPosition));
    }
}
