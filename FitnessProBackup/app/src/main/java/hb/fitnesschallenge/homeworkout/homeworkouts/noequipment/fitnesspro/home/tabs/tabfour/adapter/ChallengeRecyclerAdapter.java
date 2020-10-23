package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.adapter;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.cards.AdCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.listener.IAdCardClicked;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.cards.RateQuestionCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.card.ChallengeCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.card.ChallengeTitleCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.liseners.IBeginChallenge;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.liseners.IDaySelect;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.model.ChallengeItem;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.HeaderCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.ReminderCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners.IRateClicked;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
import java.util.List;

public class ChallengeRecyclerAdapter extends Adapter<ViewHolder> {
    private List<ChallengeItem> dataList;
    private IAdCardClicked iAdCardClicked;
    private IBeginChallenge iBeginChallenge;
    private IDaySelect iDaySelect;
    private IRateClicked iRateClicked;
    private Context mContext;
    private View mHeaderView;

    public ChallengeRecyclerAdapter(Context mContext, List<ChallengeItem> dataList, View headerView, IDaySelect iDaySelect, IBeginChallenge iBeginChallenge, IAdCardClicked iAdCardClicked, IRateClicked iRateClicked) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.iDaySelect = iDaySelect;
        this.iBeginChallenge = iBeginChallenge;
        this.iAdCardClicked = iAdCardClicked;
        this.iRateClicked = iRateClicked;
        this.mHeaderView = headerView;
    }

    public int getItemViewType(int position) {
        int rPosition;
        if (position > 0) {
            rPosition = position - 1;
        } else {
            rPosition = position;
        }
        switch (((ChallengeItem) this.dataList.get(rPosition)).getViewType()) {
            case 4:
                if (position != 0) {
                    return 4;
                }
                return 0;
            case 6:
                if (position != 0) {
                    return 6;
                }
                return 0;
            case 7:
                return position == 0 ? 0 : 7;
            case 10:
                if (position != 0) {
                    return 10;
                }
                return 0;
            default:
                if (position == 0) {
                    return 0;
                }
                return 11;
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbstractCard card;
        switch (viewType) {
            case 4:
                return new RateQuestionCard(this.mContext, parent);
            case 6:
                return new AdCard(this.mContext, parent);
            case 7:
                return new ReminderCard(this.mContext, parent);
            case 10:
                card = new ChallengeTitleCard(this.mContext, parent);
                ((ChallengeTitleCard) card).setDaySelectListener(this.iDaySelect);
                return card;
            case 11:
                card = new ChallengeCard(this.mContext, parent);
                ((ChallengeCard) card).setDaySelectListener(this.iDaySelect);
                card.setBeginChallenge(this.iBeginChallenge);
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
        ChallengeItem data = (ChallengeItem) this.dataList.get(realPosition);
        AbstractCard card = (AbstractCard) viewHolder;
        card.setiAdCardClicked(this.iAdCardClicked);
        card.setiRateClicked(this.iRateClicked);
        card.bind(data);
    }

    public int getItemCount() {
        if (this.mHeaderView == null) {
            return this.dataList.size();
        }
        return this.dataList.size() + 1;
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
            if (((ChallengeItem) it.next()).getViewType() == 6) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }
}
