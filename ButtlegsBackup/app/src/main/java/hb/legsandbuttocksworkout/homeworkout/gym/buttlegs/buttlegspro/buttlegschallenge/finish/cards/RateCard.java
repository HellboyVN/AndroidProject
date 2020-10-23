package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.finish.cards;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards.AbstractCard;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.RateHelper;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RateCard extends AbstractCard {
    private Context context;
    Typeface dRegular;
    Typeface rBold;
    Typeface rLight;
    Typeface rRegular;
    @BindView(R.id.rateBar)
    RatingBar rateBar;
    @BindView(R.id.rateCardView)
    CardView rateCardView;
    @BindView(R.id.rateOk)
    AppCompatTextView rateOk;
    @BindView(R.id.rateTitle)
    AppCompatTextView rateTitle;

    public RateCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.finish_card_rating, parent, false));
        this.context = context;
    }

    public RateCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        initFonts(context);
    }

    public void bind(Object data) {
        this.rateCardView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RateCard.this.rate(RateCard.this.context.getString(R.string.thank_you));
                RateCard.this.rateClicked(RateCard.this.getAdapterPosition());
            }
        });
    }

    private void rate(String message) {
        RateHelper.openPlayStore(this.context, this.context.getPackageName(), message);
        SharedPrefsService.getInstance().setRatedStatus(this.context, true);
    }

    private void initFonts(Context context) {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(context);
        this.dRegular = TypeFaceService.getInstance().getDensRegular(context);
        this.rBold = TypeFaceService.getInstance().getRobotoBold(context);
        setFonts();
    }

    private void setFonts() {
        this.rateTitle.setTypeface(this.rRegular);
    }
}
