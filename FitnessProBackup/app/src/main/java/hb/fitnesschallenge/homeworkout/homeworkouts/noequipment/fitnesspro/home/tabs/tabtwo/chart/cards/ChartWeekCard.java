package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.chart.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.LineChartView;

import java.util.Calendar;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.chart.linechart.WeeklyLineCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.model.TabTwoData;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartWeekCard extends MoreTabAbstractCard {
    @BindView(R.id.card1)
    CardView card1;
    @BindView(R.id.chart1)
    LineChartView chart1;
    Context context;
    private long currentTime;
    private WeeklyLineCard weeklyLineCard;

    public ChartWeekCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_chart_week, parent, false));
    }

    public ChartWeekCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        this.context = context;
        this.currentTime = Calendar.getInstance().getTimeInMillis();
    }

    public void bind(Object data) {
        TabTwoData currentData = (TabTwoData) data;
        if (currentData != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(5, currentData.getWeekOffset() * 7);
            this.currentTime = calendar.getTimeInMillis();
            this.chart1.removeAllViews();
            this.weeklyLineCard = new WeeklyLineCard(this.card1, this.context, this.currentTime);
            this.weeklyLineCard.init();
        }
    }
}
