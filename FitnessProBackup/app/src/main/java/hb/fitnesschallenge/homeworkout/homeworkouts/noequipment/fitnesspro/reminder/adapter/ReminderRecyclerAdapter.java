package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.HeaderCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.cards.SetTimeCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.data.TimeData;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.listener.ITimeItemClik;

public class ReminderRecyclerAdapter extends Adapter<ViewHolder> {
    private final int TYPE_REMINDER = 1;
    private List<TimeData> dataList;
    ITimeItemClik iTimeItemClik;
    private Context mContext;
    private View mHeaderView;

    public ReminderRecyclerAdapter(Context mContext, List<TimeData> dataList, View mHeaderView, ITimeItemClik iTimeItemClik) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.mHeaderView = mHeaderView;
        this.iTimeItemClik = iTimeItemClik;
    }

    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new SetTimeCard(this.mContext, parent);
            default:
                return new HeaderCard(this.mContext, this.mHeaderView);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int realPosition = 0;
        if (position > 0) {
            realPosition = position - 1;
        }
        if (this.dataList != null && !this.dataList.isEmpty()) {
            TimeData data = (TimeData) this.dataList.get(realPosition);
            AbstractCard card = (AbstractCard) viewHolder;
            card.setiTimerCardClicked(this.iTimeItemClik);
            card.setForFinish(false);
            card.bind(data);
        }
    }

    public int getItemCount() {
        if (this.mHeaderView == null) {
            return this.dataList.size();
        }
        return this.dataList.size() + 1;
    }

    public void removeAt(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.dataList.size());
    }
}
