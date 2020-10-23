package hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter.card.WeightStatisticsCard;
import java.util.ArrayList;
import java.util.List;

public class SRecyclerAdapter extends Adapter<ViewHolder> {
    private Activity mContext;
    private List<String> sampleData = new ArrayList();

    public SRecyclerAdapter(Activity mContext) {
        this.mContext = mContext;
        this.sampleData.add("123");
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new WeightStatisticsCard(this.mContext, parent);
            default:
                return new WeightStatisticsCard(this.mContext, parent);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ((AbstractCard) viewHolder).bind((String) this.sampleData.get(position));
    }

    public int getItemCount() {
        return this.sampleData.size();
    }
}
