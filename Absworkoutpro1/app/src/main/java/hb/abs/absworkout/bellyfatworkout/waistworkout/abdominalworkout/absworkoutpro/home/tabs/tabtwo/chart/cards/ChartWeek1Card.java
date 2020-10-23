package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.db.chart.view.LineChartView;

public class ChartWeek1Card extends MoreTabAbstractCard {
    @BindView(R.id.card2)
    CardView card2;
    @BindView(R.id.chart2)
    LineChartView chart2;
    @BindView(R.id.chart2_legend)
    TextView chart2Legend;
    @BindView(R.id.chart2_time)
    TextView chart2Time;
    Context mContext;
    @BindView(R.id.play)
    ImageButton play;
    @BindView(R.id.update)
    ImageButton update;

    public ChartWeek1Card(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_chart_week, parent, false));
    }

    public ChartWeek1Card(Context context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
    }
}
