package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.ChallengeDayCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IDaySelect;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models.ChallengeDay;
import java.util.List;

public class ChallengeDaysRecyclerAdapter extends Adapter<ViewHolder> {
    private List<ChallengeDay> dataList;
    private IDaySelect iDaySelect;
    private Context mContext;

    public ChallengeDaysRecyclerAdapter(Context mContext, List<ChallengeDay> dataList, IDaySelect iDaySelect) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.iDaySelect = iDaySelect;
    }

    public int getItemViewType(int position) {
        return ((ChallengeDay) this.dataList.get(position)).getViewType();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractCard card;
        switch (viewType) {
            case 9:
                card = new ChallengeDayCard(this.mContext, parent);
                ((ChallengeDayCard) card).setDaySelectListener(this.iDaySelect);
                return card;
            default:
                card = new ChallengeDayCard(this.mContext, parent);
                ((ChallengeDayCard) card).setDaySelectListener(this.iDaySelect);
                return card;
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ((AbstractCard) viewHolder).bind((ChallengeDay) this.dataList.get(position));
    }

    public int getItemCount() {
        return this.dataList.size();
    }
}
