package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.adapters.ChallengeDaysRecyclerAdapter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.lisener.IDaySelect;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.models.ChallengeDay;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ChallengeDaysType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.BitmapHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ShareHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;

public class ChallengeCard extends AbstractCard {
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.challengeLayout)
    LinearLayout challengeLayout;
    @BindView(R.id.congAgain)
    AppCompatTextView congAgain;
    @BindView(R.id.congImg)
    ImageView congImg;
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
    @BindView( R.id.recyclerView)
    RecyclerView mRecyclerView;
    private ChallengeDaysType mType;
    @BindView(R.id.startChallenge)
    AppCompatButton startChallenge;
    @BindView(R.id.titleChallengeDay)
    AppCompatTextView titleChallengeDay;
    private InterstitialAd interstitialAd;
    public ChallengeCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_challenge, parent, false));
        this.context = context;
    }

    public ChallengeCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
    }

    public void bind(Object data) {
        this.mType = ChallengeDaysType.getByType(SharedPrefsService.getInstance().getChallengeType(this.context));
        int challengeCurrentDay = SharedPrefsService.getInstance().getChallengeCurrentDay(this.context, this.mType.getType());
        this.startChallenge.setVisibility(challengeCurrentDay == 30 ? 8 : 0);
        setupList();
        if (challengeCurrentDay == 30) {
            showCongrats();
        } else {
            showChallenge();
        }
        this.startChallenge.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ChallengeCard.this.beginChallegeClicked(ChallengeCard.this.getAdapterPosition());
                showInterFacebook(context);
            }
        });
        this.titleChallengeDay.setText(this.context.getString(R.string.day) + " " + (challengeCurrentDay + 1));
    }
    protected void showInterFacebook(Context context){
        if(!SharedPrefsService.getInstance().getPurchasedRemoveAds(context)){
            interstitialAd = new InterstitialAd(context, "141466806566650_141467579899906");
            interstitialAd.setAdListener(new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                }

                @Override
                public void onAdLoaded(Ad ad) {
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            });
            interstitialAd.loadAd();
        }
    }
    private void showCongrats() {
        this.challengeLayout.setVisibility(4);
        this.congratsLayout.setVisibility(0);
        this.congPlanName.setText(this.context.getString(R.string.challange_30_days));
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

    private void setupList() {
        List<ChallengeDay> data = new ArrayList();
        int challengeCurrentDay = SharedPrefsService.getInstance().getChallengeCurrentDay(this.context, this.mType.getType());
        for (int i = 1; i <= 30; i++) {
            ChallengeDay challengeDay = new ChallengeDay();
            challengeDay.setDay(i);
            if (challengeDay.getDay() <= challengeCurrentDay) {
                challengeDay.setCompleted(true);
            } else if (challengeDay.getDay() > challengeCurrentDay + 1) {
                challengeDay.setDisabled(true);
            }
            data.add(challengeDay);
        }
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this.context, 6));
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
