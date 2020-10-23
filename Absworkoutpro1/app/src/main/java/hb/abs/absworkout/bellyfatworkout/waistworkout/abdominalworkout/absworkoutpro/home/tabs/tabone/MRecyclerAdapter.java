package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.ads.cards.AdCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.ads.listener.IAdCardClicked;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.finish.cards.RateQuestionCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.listener.IWeightChang;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.AbstractCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.CustomWorkoutCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.HeaderCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.LabelCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.ReminderCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.WeightCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.WorkoutCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.listeners.IRateClicked;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.LevelData;

public class MRecyclerAdapter extends Adapter<ViewHolder> {
    private List<LevelData> dataList;
    private IAdCardClicked iAdCardClicked;
    private IWeightChang iWeightChang;
    private Context mContext;
    private View mHeaderView;
    private IRateClicked rateClicked;

    public MRecyclerAdapter(Context mContext, List<LevelData> dataList, View headerView, IRateClicked rateClicked, IAdCardClicked iAdCardClicked, IWeightChang iWeightChang) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.mHeaderView = headerView;
        this.rateClicked = rateClicked;
        this.iAdCardClicked = iAdCardClicked;
        this.iWeightChang = iWeightChang;
    }

    public void update(List<LevelData> dataList) {
        this.dataList = dataList;
    }

    public int getItemViewType(int position) {
        int rPosition;
        if (position > 0) {
            rPosition = position - 1;
        } else {
            rPosition = position;
        }
        switch (((LevelData) this.dataList.get(rPosition)).getViewType()) {
            case 3:
                if (position != 0) {
                    return 3;
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
            case 7:
                if (position != 0) {
                    return 7;
                }
                return 0;
            case 8:
                if (position != 0) {
                    return 8;
                }
                return 0;
            default:
                if (position == 0) {
                    return 0;
                }
                return -1;
        }
    }

    public int getItemCount() {
        if (this.mHeaderView == null) {
            return this.dataList.size();
        }
        return this.dataList.size() + 1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case -1:
                return new WorkoutCard(this.mContext, parent);
            case 3:
                return new CustomWorkoutCard(this.mContext, parent);
            case 4:
                return new RateQuestionCard(this.mContext, parent);
            case 5:
                return new LabelCard(this.mContext, parent);
            case 6:
                return new AdCard(this.mContext, parent);
            case 7:
                return new ReminderCard(this.mContext, parent);
            case 8:
                return new WeightCard(this.mContext, parent);
            default:
                return new HeaderCard(this.mContext, this.mHeaderView);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int realPosition = 0;
        if (position > 0) {
            realPosition = position - 1;
        }
        LevelData data = (LevelData) this.dataList.get(realPosition);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setiRateClicked(this.rateClicked);
        card.setiAdCardClicked(this.iAdCardClicked);
        card.setweightChange(this.iWeightChang);
        card.bind(data);
    }

    public void removeAt(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.dataList.size());
    }

    public void removeAt(LevelData levelData) {
        int position = this.dataList.indexOf(levelData);
        this.dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.dataList.size());
    }

    public void removeAds() {
        Iterator<LevelData> it = this.dataList.iterator();
        while (it.hasNext()) {
            if (((LevelData) it.next()).getViewType() == 6) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }
}
