package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.cards.CongratsCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.cards.RateQuestionCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.cards.RecommendedCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.model.Finish;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.listeners.IRateClicked;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.cards.SetTimeCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.data.Time;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.data.TimeData;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.reminder.listener.ITimeItemClik;
import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FinishRecycleAdapter extends Adapter<ViewHolder> {
    private static final int VIEW_TYPE_CONGRATS = 0;
    private static final int VIEW_TYPE_RATE = 1;
    private static final int VIEW_TYPE_RECCOMEND = 2;
    private static final int VIEW_TYPE_REMINDER = 3;
    private Context context;
    private IRateClicked iRateClicked;
    private ITimeItemClik iTimeItemClik;
    private LayoutInflater mInflater;
    private List<Finish> mItems;
    private TimeData timeData = crateTimData();

    public FinishRecycleAdapter(Context context, List<Finish> data, IRateClicked iRateClicked, ITimeItemClik iTimeItemClik) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = data;
        this.context = context;
        this.iRateClicked = iRateClicked;
        this.iTimeItemClik = iTimeItemClik;
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
            case 1:
                return new RateQuestionCard(this.context, parent);
            case 2:
                return new RecommendedCard(this.context, parent);
            case 3:
                return new SetTimeCard(this.context, parent);
            default:
                return new CongratsCard(this.context, parent);
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        AbstractCard card = (AbstractCard) viewHolder;
        Finish finish = (Finish) this.mItems.get(position);
        card.setiRateClicked(this.iRateClicked);
        if (finish.getViewType().intValue() == 3) {
            card.setiTimerCardClicked(this.iTimeItemClik);
            card.setForFinish(true);
            card.bind(this.timeData);
            return;
        }
        card.bind(finish);
        card.setForFinish(false);
    }

    void removeAt(int position) {
        this.mItems.remove(position);
        notifyDataSetChanged();
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

    public void chengeTimeData(TimeData time) {
        this.timeData = time;
    }
}
