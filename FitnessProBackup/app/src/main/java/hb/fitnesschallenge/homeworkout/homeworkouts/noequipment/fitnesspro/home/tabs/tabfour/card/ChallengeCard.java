package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.card;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.adapter.ChallengeDaysRecyclerAdapter;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.liseners.IDaySelect;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.model.ChallengeDay;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.type.ChallengeDaysType;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.BitmapHelper;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ShareHelper;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;

public class ChallengeCard extends AbstractCard {
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.challengeLayout)
    LinearLayout challengeLayout;
    @BindView(R.id.congAgain)
    AppCompatTextView congAgain;
    @BindView(R.id.congImg)
    AppCompatImageView congImg;
    @BindView(R.id.congPlanName)
    AppCompatTextView congPlanName;
    @BindView(R.id.congShare)
    AppCompatTextView congShare;
    @BindView(R.id.congTitle)
    AppCompatTextView congTitle;
    @BindView(R.id.congratsLayout)
    LinearLayout congratsLayout;
    private Context context;
    private IDaySelect iDaySelect;
    private ChallengeDaysRecyclerAdapter mChallengeDaysRecyclerAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ChallengeDaysType mType;
    @BindView(R.id.startChallenge)
    AppCompatButton startChallenge;
    @BindView(R.id.titleChallengeDay)
    AppCompatTextView titleChallengeDay;

    public ChallengeCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_challenge, parent, false));
        this.context = context;
    }

    public ChallengeCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
    }

    public void bind(Object data) {
        int gender = SharedPrefsService.getInstance().getGender(this.context);
        this.mType = ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(this.context, gender));
        int challengeCurrentDay = SharedPrefsService.getInstance().getChallengeCurrentDay(this.context, this.mType.getType(), gender);
        this.startChallenge.setVisibility(challengeCurrentDay == 21 ? 8 : 0);
        setupList(gender);
        if (challengeCurrentDay == 21) {
            showCongrats();
        } else {
            showChallenge();
        }
        this.startChallenge.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ChallengeCard.this.beginChallegeClicked(ChallengeCard.this.getAdapterPosition());
            }
        });
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        this.titleChallengeDay.setText(this.context.getString(R.string.day) + " " + (challengeCurrentDay + 1)+" - "+ calendar.get(Calendar.DATE) +"/"+(calendar.get(Calendar.MONTH)+1));
    }

    private void showCongrats() {
        this.challengeLayout.setVisibility(4);
        this.congratsLayout.setVisibility(0);
        this.congPlanName.setText(this.context.getString(R.string.challenge));
        this.congImg.setImageBitmap(BitmapHelper.decodeSampledBitmapFromResource(this.context.getResources(), R.drawable.congrats, BitmapHelper.getScreenWidth(this.context)));
        this.congAgain.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ChallengeCard.this.iDaySelect != null) {
                    ChallengeCard.this.iDaySelect.onDayClick(null);
                }
            }
        });
        this.congShare.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ShareHelper.shareApp(ChallengeCard.this.context);
            }
        });
    }

    private void showChallenge() {
        this.challengeLayout.setVisibility(0);
        this.congratsLayout.setVisibility(8);
    }

    private void setupList(int gender) {
        List<ChallengeDay> data = new ArrayList();
        int challengeCurrentDay = SharedPrefsService.getInstance().getChallengeCurrentDay(this.context, this.mType.getType(), gender);
        for (int i = 1; i <= 21; i++) {
            ChallengeDay challengeDay = new ChallengeDay();
            challengeDay.setDay(i);
            if (challengeDay.getDay() <= challengeCurrentDay) {
                challengeDay.setCompleted(true);
            } else if (challengeDay.getDay() > challengeCurrentDay + 1) {
                challengeDay.setDisabled(true);
            }
            data.add(challengeDay);
        }
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.context, 5));
        this.mRecyclerView.setHasFixedSize(false);
        this.mRecyclerView.clearFocus();
        this.mChallengeDaysRecyclerAdapter = new ChallengeDaysRecyclerAdapter(this.context, data, this.iDaySelect);
        this.mRecyclerView.setAdapter(this.mChallengeDaysRecyclerAdapter);
    }

    public IDaySelect getDaySelectListener() {
        return this.iDaySelect;
    }

    public void setDaySelectListener(IDaySelect iDaySelect) {
        this.iDaySelect = iDaySelect;
    }
}
