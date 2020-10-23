package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.chart.linechart;

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

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabtwo.chart.CardController;

public class MonthlyLineCard extends CardController {
    private Runnable mBaseAction;
    private final LineChartView mChart;
    private final Context mContext;
    private final String[] mLabels = new String[]{"Jan", "Fev", "Mar", "Apr", "Jun", "May", "Jul", "Aug", "Sep", "Nov", "Oct", "Dec"};
    private Tooltip mTip;
    private final float[][] mValues = new float[][]{new float[]{3.5f, 4.7f, 4.3f, 8.0f, 6.5f, 9.9f, 7.0f, 8.3f, 7.0f, 0.0f, 0.0f, 0.0f}, new float[]{4.5f, 2.5f, 2.5f, 9.0f, 4.5f, 9.5f, 5.0f, 8.3f, 1.8f, 0.0f, 0.0f, 0.0f}};

    public MonthlyLineCard(CardView card, Context context) {
        super(card);
        this.mContext = context;
        this.mChart = (LineChartView) card.findViewById(R.id.chart2);
    }

    public void show(Runnable action) {
        super.show(action);
        this.mTip = new Tooltip(this.mContext, R.layout.linechart_three_tooltip, R.id.value);
        this.mTip.setVerticalAlignment(Alignment.BOTTOM_TOP);
        this.mTip.setDimensions((int) Tools.fromDpToPx(58.0f), (int) Tools.fromDpToPx(25.0f));
        if (VERSION.SDK_INT >= 14) {
            Tooltip tooltip = this.mTip;
            PropertyValuesHolder[] r4 = new PropertyValuesHolder[3];
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
        LineSet dataset = new LineSet(this.mLabels, this.mValues[0]);
        dataset.setColor(Color.parseColor("#758cbb")).setFill(Color.parseColor("#2d374c")).setDotsColor(Color.parseColor("#758cbb")).setThickness(4.0f).setDashed(new float[]{10.0f, 10.0f}).beginAt(5);
        this.mChart.addData((ChartSet) dataset);
        dataset = new LineSet(this.mLabels, this.mValues[0]);
        dataset.setColor(Color.parseColor("#b3b5bb")).setFill(Color.parseColor("#2d374c")).setDotsColor(Color.parseColor("#ffc755")).setThickness(4.0f).endAt(6);
        this.mChart.addData((ChartSet) dataset);
        this.mChart.setBorderSpacing((int)Tools.fromDpToPx(15.0f)).setAxisBorderValues(0, 20).setYLabels(LabelPosition.NONE).setLabelsColor(Color.parseColor("#6a84c3")).setXAxis(false).setYAxis(false);
        this.mBaseAction = action;
        this.mChart.show(new Animation().setInterpolator(new BounceInterpolator()).withEndAction(new Runnable() {
            public void run() {
                MonthlyLineCard.this.mBaseAction.run();
                MonthlyLineCard.this.mTip.prepare((Rect) MonthlyLineCard.this.mChart.getEntriesArea(0).get(3), MonthlyLineCard.this.mValues[0][3]);
                MonthlyLineCard.this.mChart.showTooltip(MonthlyLineCard.this.mTip, true);
            }
        }));
    }

    public void update() {
        super.update();
        this.mChart.dismissAllTooltips();
        if (this.firstStage) {
            this.mChart.updateValues(0, this.mValues[1]);
            this.mChart.updateValues(1, this.mValues[1]);
        } else {
            this.mChart.updateValues(0, this.mValues[0]);
            this.mChart.updateValues(1, this.mValues[0]);
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
