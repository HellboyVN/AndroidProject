package hb.me.homeworkout.gym.buttlegs.buttlegspro.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards.RateQuestionCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.CustomWorkoutCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.LabelCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.ReminderCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.WorkoutCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;

public class MRecyclerAdapter extends Adapter<ViewHolder> {
    private List<LevelData> dataList;
    IAdCardClicked iAdCardClicked;
    IRateClicked iRateClicked;
    private Context mContext;

    public MRecyclerAdapter(Context mContext, List<LevelData> dataList, IRateClicked iRateClicked, IAdCardClicked iAdCardClicked) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.iRateClicked = iRateClicked;
        this.iAdCardClicked = iAdCardClicked;
    }

    public int getItemViewType(int position) {
        return ((LevelData) this.dataList.get(position)).getViewType();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new LabelCard(this.mContext, parent);
            case 2:
                return new RateQuestionCard(this.mContext, parent);
            case 3:
                return new CustomWorkoutCard(this.mContext, parent);
//            case 4:
//                return new AdCard(this.mContext, parent);
            case 7:
                return new ReminderCard(this.mContext, parent);
            default:
                return new WorkoutCard(this.mContext, parent);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        LevelData data = (LevelData) this.dataList.get(position);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setRateListener(this.iRateClicked);
        card.setiAdCardClicked(this.iAdCardClicked);
        card.bind(data);
    }

    public int getItemCount() {
        return this.dataList.size();
    }

    public void removeAt(int position) {
        if (position >= 0 && position < this.dataList.size()) {
            this.dataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.dataList.size());
        }
    }

    public void removeAt(LevelData levelData) {
        int position = this.dataList.indexOf(levelData);
        if (position >= 0 && position <= this.dataList.size() - 1) {
            this.dataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.dataList.size());
        }
    }

    public void removeAds() {
        Iterator<LevelData> it = this.dataList.iterator();
        while (it.hasNext()) {
            if (((LevelData) it.next()).getViewType() == 4) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }
}
