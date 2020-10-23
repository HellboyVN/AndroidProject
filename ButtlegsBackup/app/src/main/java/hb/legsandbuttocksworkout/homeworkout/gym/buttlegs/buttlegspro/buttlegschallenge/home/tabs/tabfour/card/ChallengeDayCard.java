package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.card;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.SquareRelativeLayout;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.liseners.IDaySelect;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabfour.model.ChallengeDay;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards.AbstractCard;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChallengeDayCard extends AbstractCard {
    @BindView(R.id.dayLayout)
    SquareRelativeLayout dayLayout;
    IDaySelect iDaySelect;
    @BindView(R.id.icCheck)
    AppCompatImageView icCheck;
    @BindView(R.id.title)
    AppCompatTextView title;

    public ChallengeDayCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_challenge_day, parent, false));
    }

    public ChallengeDayCard(Context context, View view) {
        super(view, context);
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
        final ChallengeDay challengeDay = (ChallengeDay) data;
        if (challengeDay.isDisabled()) {
            this.dayLayout.setBackgroundResource(R.drawable.day_card_bg_disabled);
            this.title.setVisibility(0);
            this.title.setTextColor(ContextCompat.getColor(this.context, R.color.white));
            this.icCheck.setVisibility(4);
        } else if (challengeDay.isCompleted()) {
            this.dayLayout.setBackgroundResource(R.drawable.day_card_bg_completed);
            this.title.setVisibility(4);
            this.icCheck.setVisibility(0);
        } else {
            this.dayLayout.setBackgroundResource(R.drawable.day_card_selector);
            this.title.setVisibility(0);
            this.icCheck.setVisibility(4);
        }
        this.title.setText(String.valueOf(challengeDay.getDay()));
        this.title.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.dayLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ChallengeDayCard.this.iDaySelect != null && !challengeDay.isDisabled()) {
                    ChallengeDayCard.this.iDaySelect.onDayClick(challengeDay);
                }
            }
        });
    }

    public IDaySelect getDaySelectListener() {
        return this.iDaySelect;
    }

    public void setDaySelectListener(IDaySelect iDaySelect) {
        this.iDaySelect = iDaySelect;
    }
}
