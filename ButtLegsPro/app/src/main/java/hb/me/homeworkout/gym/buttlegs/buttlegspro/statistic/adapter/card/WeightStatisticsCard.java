package hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter.card;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.RealmManager;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.User;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.modelweight.UserWeight;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter.card.WeightChartData.WeightChartMonthLine;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.statistic.adapter.card.WeightChartData.WeightChartRecord;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.DialogHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeightStatisticsCard extends AbstractCard {
    @BindView(R.id.btnAddWeight)
    AppCompatImageView btnAddWeight;
    @BindView(R.id.currentWeight)
    AppCompatTextView currentWeight;
    @BindView( R.id.labelWeight)
    TextView labelWeight;
    @BindView(R.id.layoutCurrent)
    LinearLayout layoutCurrent;
    @BindView(R.id.lineChart)
    LineChart mChart;
    Activity mContext;
    private User user;

    public class TimeXAxisValueFormatter implements IAxisValueFormatter {
        private Calendar calendar = Calendar.getInstance();
        private SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        private SimpleDateFormat dayWithMonthFormat = new SimpleDateFormat("MMM dd");
        private long mTimeStart;

        public TimeXAxisValueFormatter(long timeStart) {
            this.mTimeStart = timeStart;
        }

        public String getFormattedValue(float value, AxisBase axis) {
            this.calendar.setTimeInMillis(this.mTimeStart);
            this.calendar.add(5, (int) value);
            return this.dayFormat.format(this.calendar.getTime());
        }
    }

    public WeightStatisticsCard(Activity context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_weight_statistics, parent, false));
    }

    public WeightStatisticsCard(Activity context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WeightChartData getChartData() {
        WeightChartData weightChartData = new WeightChartData(this.mContext);
        weightChartData.parse(RealmManager.getInstance().getWeightRecords(this.user), this.user.getWeightUnit());
        return weightChartData;
    }

    public void bind(Object data) {
        String string;
        this.user = RealmManager.getInstance().getUser();
        WeightChartData weightChartData = getChartData();
        this.btnAddWeight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getInstance().showWeightInsertDialog(WeightStatisticsCard.this.mContext);
            }
        });
        int metric = RealmManager.getInstance().getUser().getWeightUnit();
        StringBuilder append = new StringBuilder().append(this.context.getString(R.string.weight)).append(" (");
        if (metric == 0) {
            string = this.context.getString(R.string.kg);
        } else {
            string = this.context.getString(R.string.lb);
        }
        this.labelWeight.setText(append.append(string).append(")").toString());
        this.mChart.setDrawGridBackground(false);
        this.mChart.setDragEnabled(true);
        this.mChart.setScaleEnabled(true);
        this.mChart.setScaleYEnabled(false);
        this.mChart.setScaleXEnabled(true);
        this.mChart.getDescription().setText("");
        XAxis xAxis = this.mChart.getXAxis();
        xAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        xAxis.removeAllLimitLines();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum((float) weightChartData.getMinDayOffset());
        xAxis.setAxisMaximum((float) weightChartData.getMaxDayOffset());
        xAxis.setValueFormatter(new TimeXAxisValueFormatter(weightChartData.getStartTime()));
        for (WeightChartMonthLine line : weightChartData.getMonthLines()) {
            LimitLine llXAxis = new LimitLine((float) line.getValue(), line.getName());
            llXAxis.setLineWidth(1.0f);
            llXAxis.enableDashedLine(10.0f, 10.0f, 0.0f);
            llXAxis.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(8.0f);
            xAxis.addLimitLine(llXAxis);
        }
        YAxis leftAxis = this.mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.setAxisMaximum((float) weightChartData.getMaxWeight());
        leftAxis.setAxisMinimum((float) weightChartData.getMinWeight());
        leftAxis.enableGridDashedLine(10.0f, 10.0f, 0.0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(true);
        this.mChart.getAxisRight().setEnabled(false);
        setData(weightChartData);
        this.mChart.setVisibleXRangeMinimum(8.0f);
        this.mChart.setVisibleXRangeMaximum(60.0f);
        this.mChart.moveViewToX((float) weightChartData.getMaxDayOffset());
        this.mChart.moveViewToX((float) (weightChartData.getMaxDayOffset() - 7));
        this.mChart.getLegend().setForm(LegendForm.LINE);
        UserWeight currUserWeight = RealmManager.getInstance().getLastWeightRecord();
        if (currUserWeight != null) {
            this.currentWeight.setText(currUserWeight.getWeight(metric) + " " + (metric == 0 ? this.context.getString(R.string.kg) : this.context.getString(R.string.lb)));
            this.layoutCurrent.setVisibility(0);
            return;
        }
        this.layoutCurrent.setVisibility(8);
    }

    private void setData(WeightChartData weightChartData) {
        ArrayList<Entry> values = new ArrayList();
        for (WeightChartRecord weightChartRecord : weightChartData.getWeightRecords()) {
            values.add(new Entry((float) weightChartRecord.getDaysFromStart(), weightChartRecord.getWeight()));
        }
        int color = ContextCompat.getColor(getContext(), R.color.colorPrimaryDark);
        if (this.mChart.getData() == null || ((LineData) this.mChart.getData()).getDataSetCount() <= 0) {
            LineDataSet set1 = new LineDataSet(values, this.context.getString(R.string.weight));
            set1.setDrawIcons(false);
            set1.setColor(color);
            set1.setLineWidth(3.0f);
            set1.setCircleRadius(4.0f);
            set1.setDrawCircles(true);
            set1.setCircleColor(color);
            set1.setDrawValues(false);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9.0f);
            if (Utils.getSDKInt() >= 18) {
                set1.setFillDrawable(ContextCompat.getDrawable(this.mContext, R.drawable.fade_red));
            } else {
                set1.setFillColor(ViewCompat.MEASURED_STATE_MASK);
            }
            List dataSets = new ArrayList();
            dataSets.add(set1);
            this.mChart.setData(new LineData(dataSets));
            return;
        }
        ((LineDataSet) ((LineData) this.mChart.getData()).getDataSetByIndex(0)).setValues(values);
        ((LineData) this.mChart.getData()).notifyDataChanged();
        this.mChart.notifyDataSetChanged();
    }
}
