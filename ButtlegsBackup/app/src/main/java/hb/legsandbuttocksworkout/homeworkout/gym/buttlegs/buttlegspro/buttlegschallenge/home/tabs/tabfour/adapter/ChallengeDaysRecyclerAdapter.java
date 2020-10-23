package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.adapter;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.card.ChallengeDayCard;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.liseners.IDaySelect;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.model.ChallengeDay;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards.AbstractCard;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
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
