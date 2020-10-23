package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IDaySelect;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ChallengeDaysType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;

public class ChallengeTitleCard extends AbstractCard {
    private IDaySelect iDaySelect;
    @BindView(R.id.icChangePlan)
    AppCompatImageView icChangePlan;
    @BindView(R.id.icResetProgress)
    AppCompatImageView icResetProgress;
    @BindView(R.id.planCardLayout)
    CardView planCardLayout;
    @BindView(R.id.planTitle)
    AppCompatTextView planTitle;

    public ChallengeTitleCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_challenge_title, parent, false));
    }

    public ChallengeTitleCard(Context context, View view) {
        super(view, context);
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
        this.planCardLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ChallengeTitleCard.this.getDaySelectListener() != null) {
                    ChallengeTitleCard.this.getDaySelectListener().onChangePlanClick();
                }
            }
        });
        this.icResetProgress.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ChallengeTitleCard.this.getDaySelectListener() != null) {
                    ChallengeTitleCard.this.getDaySelectListener().onResetProgressClick();
                }
            }
        });
        this.planTitle.setText(getUnderlineContent(this.context.getString(ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(this.context)).getTitleResId())));
    }

    private SpannableString getUnderlineContent(String text) {
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        return content;
    }

    public IDaySelect getDaySelectListener() {
        return this.iDaySelect;
    }

    public void setDaySelectListener(IDaySelect iDaySelect) {
        this.iDaySelect = iDaySelect;
    }
}
