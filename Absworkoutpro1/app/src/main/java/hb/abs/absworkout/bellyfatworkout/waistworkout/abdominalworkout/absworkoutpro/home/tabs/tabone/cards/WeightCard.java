package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards;

import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener;

import java.util.Calendar;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.LevelData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.AnimationService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SharedPrefsService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeightCard extends AbstractCard implements OnDateSetListener {
    private Calendar calendar;
    @BindView(R.id.enter)
    AppCompatTextView enter;
    @BindView(R.id.kg)
    AppCompatTextView kg;
    @BindView(R.id.kg_or_lb)
    AppCompatTextView kgLb;
    @BindView(R.id.lb)
    AppCompatTextView lb;
    private Context mContext;
    @BindView(R.id.weight_ok)
    AppCompatTextView ok;
    private View v;
    @BindView(R.id.weight_calendar)
    AppCompatTextView weightCalendar;
    @BindView(R.id.weight_count)
    AppCompatEditText weightCount;
    @BindView(R.id.weightCounteLayout)
    LinearLayout weightCounteLayout;
    @BindView(R.id.weightEnterLayout)
    FrameLayout weightEnterLayout;
    @BindView(R.id.weightMetricLayout)
    FrameLayout weightMetricLayout;

    public enum WeightCardStates {
        STATE_ENTER,
        STATE_METRIC,
        STATE_COUNT;

        public WeightCardStates getNextState() {
            switch (this) {
                case STATE_ENTER:
                    return STATE_METRIC;
                case STATE_METRIC:
                    return STATE_COUNT;
                default:
                    return STATE_COUNT;
            }
        }
    }

    public WeightCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_weight, parent, false));
    }

    public WeightCard(Context context, View view) {
        super(view, context);
        this.mContext = context;
        this.v = view;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
        this.calendar = Calendar.getInstance();
        setCalendarText();
        final LevelData levelData = new LevelData();
        if (!SharedPrefsService.getInstance().isFirstWeight(this.context)) {
            levelData.setWeightCardStates(WeightCardStates.STATE_COUNT);
        }
        procesAnswer(levelData);
        this.weightEnterLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                levelData.setWeightCardStates(levelData.getWeightCardStates().getNextState());
                WeightCard.this.procesAnswer(levelData);
            }
        });
        this.kg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPrefsService.getInstance().setWeightMetric(WeightCard.this.context, 0);
                levelData.setWeightCardStates(levelData.getWeightCardStates().getNextState());
                SharedPrefsService.getInstance().setFirstWeight(WeightCard.this.context, false);
                WeightCard.this.procesAnswer(levelData);
            }
        });
        this.lb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SharedPrefsService.getInstance().setWeightMetric(WeightCard.this.context, 1);
                levelData.setWeightCardStates(levelData.getWeightCardStates().getNextState());
                SharedPrefsService.getInstance().setFirstWeight(WeightCard.this.context, false);
                WeightCard.this.procesAnswer(levelData);
            }
        });
        this.ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (WeightCard.this.weightCount.getText().toString().equals("")) {
                    WeightCard.this.weightCount.setHintTextColor(SupportMenu.CATEGORY_MASK);
                    return;
                }
                WeightCard.this.rateClicked(WeightCard.this.getAdapterPosition());
                SharedPrefsService.getInstance().setWeightCount(WeightCard.this.context, Integer.parseInt(WeightCard.this.weightCount.getText().toString()));
                Calendar calendar = Calendar.getInstance();
                calendar.set(5, calendar.get(5) + 2);
                SharedPrefsService.getInstance().setWeightLastCalendar(WeightCard.this.context, calendar.getTimeInMillis());
            }
        });
        this.kgLb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WeightCard.this.weightMetricChange();
            }
        });
        this.weightCount.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    WeightCard.this.weightEditTextFocus(v);
                }
            }
        });
        this.weightCalendar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WeightCard.this.weightCalendarClick(WeightCard.this, WeightCard.this.calendar.get(1), WeightCard.this.calendar.get(2), WeightCard.this.calendar.get(5));
            }
        });
    }

    private void procesAnswer(LevelData levelData) {
        switch (levelData.getWeightCardStates()) {
            case STATE_ENTER:
                this.weightEnterLayout.setVisibility(0);
                this.weightMetricLayout.setVisibility(8);
                this.weightCounteLayout.setVisibility(8);
                return;
            case STATE_METRIC:
                this.weightMetricLayout.startAnimation(AnimationService.getInstance().getRight());
                this.weightMetricLayout.setVisibility(0);
                this.weightEnterLayout.startAnimation(AnimationService.getInstance().getGoLeft());
                this.weightEnterLayout.setVisibility(8);
                this.weightCounteLayout.setVisibility(8);
                return;
            case STATE_COUNT:
                if (this.weightCounteLayout.getVisibility() == 8) {
                    this.weightCounteLayout.startAnimation(AnimationService.getInstance().getRight());
                    this.weightCounteLayout.setVisibility(0);
                    this.weightMetricLayout.startAnimation(AnimationService.getInstance().getGoLeft());
                    this.weightMetricLayout.setVisibility(8);
                }
                this.weightEnterLayout.setVisibility(8);
                this.kgLb.setText(SharedPrefsService.getInstance().getWeightMetric(this.context) != 1 ? this.context.getString(R.string.kg) : this.context.getString(R.string.lb));
                return;
            default:
                return;
        }
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.calendar.set(1, year);
        this.calendar.set(2, monthOfYear);
        this.calendar.set(5, dayOfMonth);
        setCalendarText();
    }

    private void setCalendarText() {
        this.weightCalendar.setText(this.calendar.get(5) + "/" + this.calendar.get(2) + "/" + this.calendar.get(1));
    }
}
