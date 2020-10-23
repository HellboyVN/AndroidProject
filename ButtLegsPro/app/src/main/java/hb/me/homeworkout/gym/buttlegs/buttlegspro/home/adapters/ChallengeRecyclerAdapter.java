package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;

import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards.RateQuestionCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.ChallengeCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.ChallengeTitleCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.ReminderCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IBeginChallenge;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IDaySelect;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models.ChallengeItem;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;

public class ChallengeRecyclerAdapter extends Adapter<ViewHolder> {
    private List<ChallengeItem> dataList;
    private IAdCardClicked iAdCardClicked;
    private IBeginChallenge iBeginChallenge;
    private IDaySelect iDaySelect;
    private IRateClicked iRateClicked;
    private Context mContext;

    public ChallengeRecyclerAdapter(Context mContext, List<ChallengeItem> dataList, IDaySelect iDaySelect, IBeginChallenge iBeginChallenge, IAdCardClicked iAdCardClicked, IRateClicked iRateClicked) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.iDaySelect = iDaySelect;
        this.iBeginChallenge = iBeginChallenge;
        this.iAdCardClicked = iAdCardClicked;
        this.iRateClicked = iRateClicked;
    }

    public int getItemViewType(int position) {
        return ((ChallengeItem) this.dataList.get(position)).getViewType();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractCard card;
        switch (viewType) {
            case 2:
                return new RateQuestionCard(this.mContext, parent);
//            case 4:
//                return new AdCard(this.mContext, parent);
            case 7:
                return new ReminderCard(this.mContext, parent);
            case 10:
                card = new ChallengeTitleCard(this.mContext, parent);
                ((ChallengeTitleCard) card).setDaySelectListener(this.iDaySelect);
                return card;
            default:
                card = new ChallengeCard(this.mContext, parent);
                ((ChallengeCard) card).setDaySelectListener(this.iDaySelect);
                card.setBeginChallenge(this.iBeginChallenge);
                return card;
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ChallengeItem data = (ChallengeItem) this.dataList.get(position);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setiAdCardClicked(this.iAdCardClicked);
        card.setRateListener(this.iRateClicked);
        card.bind(data);
    }

    public int getItemCount() {
        return this.dataList.size();
    }

    public void removeAt(ChallengeItem challengeItem) {
        if (challengeItem != null) {
            int position = this.dataList.indexOf(challengeItem);
            if (position >= 0 && position <= this.dataList.size() - 1) {
                this.dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, this.dataList.size());
            }
        }
    }

    public void removeAt(int position) {
        if (position >= 0 && position < this.dataList.size()) {
            this.dataList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.dataList.size());
        }
    }

    public void removeAds() {
        Iterator<ChallengeItem> it = this.dataList.iterator();
        while (it.hasNext()) {
            if (((ChallengeItem) it.next()).getViewType() == 4) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }
}
