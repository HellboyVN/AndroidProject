package hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.List;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.cards.SetTimeCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.listener.ITimeItemClick;

public class ReminderRecyclerAdapter extends Adapter<ViewHolder> {
    private List<TimeData> dataList;
    ITimeItemClick iTimeItemClick;
    private Context mContext;

    public ReminderRecyclerAdapter(Context mContext, List<TimeData> dataList, ITimeItemClick iTimeItemClick) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.iTimeItemClick = iTimeItemClick;
    }

    public int getItemViewType(int position) {
        return ((TimeData) this.dataList.get(position)).getViewType();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SetTimeCard(this.mContext, parent);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        TimeData data = (TimeData) this.dataList.get(position);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setiTimerCardClicked(this.iTimeItemClick);
        card.bind(data);
    }

    public int getItemCount() {
        return this.dataList.size();
    }

    public void removeAt(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.dataList.size());
    }
}
