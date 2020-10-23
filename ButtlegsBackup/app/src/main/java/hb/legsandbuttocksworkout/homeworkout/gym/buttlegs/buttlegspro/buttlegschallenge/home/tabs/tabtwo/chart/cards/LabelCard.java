package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.chart.cards;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.database.RealmManager;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.IWeekChange;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.model.TabTwoData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.GeneralMethods;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LabelCard extends MoreTabAbstractCard {
    @BindView(R.id.mLabelTitle)
    TextView mLabelTitle;
    private IWeekChange mListener;
    @BindView(R.id.nextWeek)
    AppCompatImageView nextWeek;
    @BindView(R.id.prevWeek)
    AppCompatImageView prevWeek;
    @BindView(R.id.statsArrowLayout)
    LinearLayout statsArrowLayout;

    public LabelCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.layout_stats_perioud, parent, false));
    }

    public LabelCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        GeneralMethods.rippleEfect(this.prevWeek);
        GeneralMethods.rippleEfect(this.nextWeek);
    }

    public void setWeekChangeListener(IWeekChange listener) {
        this.mListener = listener;
    }

    public void bind(Object data) {
        TabTwoData currentData = (TabTwoData) data;
        if (currentData == null) {
            return;
        }
        if (currentData.getName() == null) {
            this.statsArrowLayout.setVisibility(0);
            this.mLabelTitle.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
            int weekStart = SharedPrefsService.getInstance().getWeekStart(this.context);
            int weekOffset = currentData.getWeekOffset();
            this.mLabelTitle.setText(RealmManager.getInstance().getWeekStartFormatted(weekStart, weekOffset) + " - " + RealmManager.getInstance().getWeekEndFormatted(weekStart, weekOffset));
            if (weekOffset >= 0) {
                this.nextWeek.setEnabled(false);
                this.nextWeek.setImageResource(R.drawable.ic_arrow_right_disable);
            } else {
                this.nextWeek.setEnabled(true);
                this.nextWeek.setImageResource(R.drawable.ic_arrow_right);
            }
            this.prevWeek.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LabelCard.this.mListener.onWeekChange(-1);
                }
            });
            this.nextWeek.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LabelCard.this.mListener.onWeekChange(1);
                }
            });
            return;
        }
        this.mLabelTitle.setText(currentData.getName());
        this.mLabelTitle.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.statsArrowLayout.setVisibility(8);
    }
}
