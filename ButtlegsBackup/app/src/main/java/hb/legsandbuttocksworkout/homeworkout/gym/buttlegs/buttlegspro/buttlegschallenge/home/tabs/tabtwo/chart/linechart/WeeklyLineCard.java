package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.chart.linechart;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.db.chart.animation.Animation;
import com.db.chart.model.ChartSet;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer.LabelPosition;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.tooltip.Tooltip.Alignment;
import com.db.chart.util.Tools;
import com.db.chart.view.LineChartView;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.model.Workout;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.model.Workout.WeeklyWorkoutForChart;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.chart.CardController;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;

public class WeeklyLineCard extends CardController {
    private Runnable mBaseAction;
    private final LineChartView mChart;
    private Context mContext = null;
    private long mTime;
    private Tooltip mTip;
    private String[] mWeekLabels;
    float[] mWeekValues;
    private Map<Integer, WeeklyWorkoutForChart> weekMap;
    private int weekStartPosition;
    WeeklyWorkoutForChart weeklyWorkoutForChart;

    public WeeklyLineCard(CardView card, Context context, long time) {
        super(card);
        this.mTime = time;
        this.mContext = context;
        weekStartPosition = SharedPrefsService.getInstance().getWeekStart(mContext);
        this.mChart = (LineChartView) card.findViewById(R.id.chart1);
        this.mWeekLabels = context.getResources().getStringArray(R.array.week_days_short);
        if (this.weekStartPosition == 0) {
            String tmp = this.mWeekLabels[this.mWeekLabels.length - 1];
            for (int i = this.mWeekLabels.length - 1; i > 0; i--) {
                this.mWeekLabels[i] = this.mWeekLabels[i - 1];
            }
            this.mWeekLabels[0] = tmp;
        }
        this.weekMap = new HashMap();
        updateData();
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public void updateData() {
        this.weeklyWorkoutForChart = Workout.getWeekData(this.mContext, this.mTime, this.weekStartPosition);
        this.mWeekValues = this.weeklyWorkoutForChart.getResult();
    }

    public void show(Runnable action) {
        super.show(action);
        this.mChart.reset();
        this.mTip = new Tooltip(this.mContext, R.layout.linechart_three_tooltip, R.id.value);
        this.mTip.setVerticalAlignment(Alignment.BOTTOM_TOP);
        this.mTip.setDimensions((int) Tools.fromDpToPx(58.0f), (int) Tools.fromDpToPx(25.0f));
        PropertyValuesHolder [] r4 = null;
        if (VERSION.SDK_INT >= 14) {
            Tooltip tooltip = this.mTip;
            r4 = new PropertyValuesHolder[3];
            r4[0] = PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{1.0f});
            r4[1] = PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0f});
            r4[2] = PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0f});
            tooltip.setEnterAnimation(r4).setDuration(200);
            tooltip = this.mTip;
            r4 = new PropertyValuesHolder[3];
            r4[0] = PropertyValuesHolder.ofFloat(View.ALPHA, new float[]{0.0f});
            r4[1] = PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{0.0f});
            r4[2] = PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{0.0f});
            tooltip.setExitAnimation(r4).setDuration(200);
            this.mTip.setPivotX(Tools.fromDpToPx(65.0f) / 2.0f);
            this.mTip.setPivotY(Tools.fromDpToPx(25.0f));
        }
        this.mChart.setTooltips(this.mTip);
        LineSet dataset = new LineSet(this.mWeekLabels, this.mWeekValues);
        if (isCurrentWeek()) {
            dataset.setColor(Color.parseColor("#009aff")).setFill(Color.parseColor("#ffbf00")).setDotsColor(Color.parseColor("#fefefe")).setThickness(4.0f).setDashed(new float[]{10.0f, 10.0f}).beginAt(this.weeklyWorkoutForChart.getCurrentDay());
            this.mChart.addData((ChartSet) dataset);
            dataset = new LineSet(this.mWeekLabels, this.mWeekValues);
            dataset.setColor(Color.parseColor("#b3b5bb")).setFill(Color.parseColor("#ffbf00")).setDotsColor(Color.parseColor("#00e6ff")).setThickness(4.0f).endAt(this.weeklyWorkoutForChart.getCurrentDay() + 1);
            this.mChart.addData((ChartSet) dataset);
        } else {
            dataset.setColor(Color.parseColor("#b3b5bb")).setFill(Color.parseColor("#ffbf00")).setDotsColor(Color.parseColor("#00e6ff")).setThickness(4.0f).endAt(this.mWeekLabels.length);
            this.mChart.addData((ChartSet) dataset);
        }
        this.mChart.setBorderSpacing((int) Tools.fromDpToPx(15.0f)).setAxisBorderValues(0, roundUp(this.weeklyWorkoutForChart.getMaxValue())).setYLabels(LabelPosition.OUTSIDE).setLabelsColor(Color.parseColor("#ffffff")).setLabelsFormat(new DecimalFormat("##' " + this.mContext.getString(R.string.minute_short) + "'")).setXAxis(false).setYAxis(false);
        this.mBaseAction = action;
        this.mChart.show(new Animation().setInterpolator(new BounceInterpolator()).withEndAction(new Runnable() {
            public void run() {
                WeeklyLineCard.this.mBaseAction.run();
                if (WeeklyLineCard.this.isCurrentWeek()) {
                    WeeklyLineCard.this.mTip.prepare((Rect) WeeklyLineCard.this.mChart.getEntriesArea(0).get(WeeklyLineCard.this.weeklyWorkoutForChart.getCurrentDay()), WeeklyLineCard.this.mWeekValues[WeeklyLineCard.this.weeklyWorkoutForChart.getCurrentDay()]);
                    WeeklyLineCard.this.mChart.showTooltip(WeeklyLineCard.this.mTip, true);
                }
            }
        }));
    }

    int roundUp(int n) {
        if (n < 6) {
            return 6;
        }
        return ((n + 4) / 5) * 5;
    }

    boolean isCurrentWeek() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(1);
        int currentWeek = calendar.get(3);
        calendar.setTimeInMillis(this.mTime);
        int tmpYear = calendar.get(1);
        int tmpWeek = calendar.get(3);
        if (tmpYear < currentYear || tmpWeek < currentWeek) {
            return false;
        }
        return true;
    }

    public void update() {
        updateData();
        super.update();
        this.mChart.dismissAllTooltips();
        if (this.firstStage) {
            this.mChart.updateValues(0, this.mWeekValues);
            this.mChart.updateValues(1, this.mWeekValues);
        } else {
            this.mChart.updateValues(0, this.mWeekValues);
            this.mChart.updateValues(1, this.mWeekValues);
        }
        this.mChart.getChartAnimation().withEndAction(this.mBaseAction);
        this.mChart.notifyDataUpdate();
    }

    public void dismiss(Runnable action) {
        super.dismiss(action);
        this.mChart.dismissAllTooltips();
        this.mChart.dismiss(new Animation().setInterpolator(new BounceInterpolator()).withEndAction(action));
    }
}
