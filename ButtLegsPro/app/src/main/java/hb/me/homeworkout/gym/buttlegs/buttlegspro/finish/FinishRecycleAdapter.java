package hb.me.homeworkout.gym.buttlegs.buttlegspro.finish;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards.CongratsCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards.RateQuestionCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards.RecommendedCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.model.Finish;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IRateClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.cards.SetTimeCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.Time;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.data.TimeData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.reminder.listener.ITimeItemClick;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class FinishRecycleAdapter extends Adapter<ViewHolder> {
    private static final int VIEW_TYPE_CONGRATS = 0;
    private static final int VIEW_TYPE_RATE = 1;
    private static final int VIEW_TYPE_RECCOMEND = 3;
    private static final int VIEW_TYPE_REMINDER = 2;
    private Context context;
    private IAdCardClicked iAdCardClicked;
    IRateClicked iRateClicked;
    private ITimeItemClick iTimeItemClick;
    private List<Finish> mItems;
    private TimeData timeData = crateTimData();

    public FinishRecycleAdapter(Context context, List<Finish> data, IAdCardClicked iAdCardClicked, ITimeItemClick iTimeItemClick, IRateClicked iRateClicked) {
        this.mItems = data;
        this.context = context;
        this.iAdCardClicked = iAdCardClicked;
        this.iTimeItemClick = iTimeItemClick;
        this.iRateClicked = iRateClicked;
    }

    public int getItemCount() {
        return this.mItems.size();
    }

    public int getItemViewType(int position) {
        switch (((Finish) this.mItems.get(position)).getViewType().intValue()) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new CongratsCard(this.context, parent);
            case 1:
                return new RateQuestionCard(this.context, parent);
            case 2:
                return new SetTimeCard(this.context, parent);
            case 3:
                return new RecommendedCard(this.context, parent);
            default:
                return new CongratsCard(this.context, parent);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        AbstractCard card = (AbstractCard) viewHolder;
        Finish finish = (Finish) this.mItems.get(position);
        card.setiAdCardClicked(this.iAdCardClicked);
        card.setRateListener(this.iRateClicked);
        if (finish.getViewType().intValue() == 2) {
            card.setiTimerCardClicked(this.iTimeItemClick);
            card.setForFinish(true);
            card.bind(this.timeData);
            return;
        }
        card.bind(finish);
    }

    public TimeData crateTimData() {
        this.timeData = new TimeData();
        List<Boolean> listDay = new ArrayList();
        for (int i = 0; i < 7; i++) {
            listDay.add(Boolean.valueOf(false));
        }
        listDay.set(Calendar.getInstance().get(7) - 1, Boolean.valueOf(true));
        this.timeData.setTime(new Time(Calendar.getInstance().get(11), Calendar.getInstance().get(12)));
        this.timeData.setListDay(listDay);
        return this.timeData;
    }

    public void changeTimeData(TimeData time) {
        this.timeData = time;
    }

    public void removeAt(Finish finish) {
        int position = this.mItems.indexOf(finish);
        this.mItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, this.mItems.size());
    }

    void removeAtPosition(int position) {
        this.mItems.remove(position);
        notifyDataSetChanged();
    }

    public void removeAds() {
        Iterator<Finish> it = this.mItems.iterator();
        while (it.hasNext()) {
            if (((Finish) it.next()).getViewType().intValue() == 4) {
                it.remove();
            }
        }
        notifyDataSetChanged();
    }
}
