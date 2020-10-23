package hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.model.Finish;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.BitmapHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class CongratsCard extends AbstractCard {
    @BindView(R.id.congAgain)
    AppCompatTextView congAgain;
    @BindView(R.id.congCard)
    CardView congCard;
    @BindView(R.id.congImg)
    ImageView congImg;
    @BindView(R.id.congPlanName)
    AppCompatTextView congPlanName;
    @BindView(R.id.congPlanTime)
    AppCompatTextView congPlanTime;
    @BindView(R.id.congShare)
    AppCompatTextView congShare;
    @BindView(R.id.congTitle)
    AppCompatTextView congTitle;
    private Context context;
    Typeface dRegular;
    Typeface rBold;
    Typeface rLight;
    Typeface rRegular;

    public CongratsCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.finish_card_congrats, parent, false));
        this.context = context;
    }

    public CongratsCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        initFonts(context);
    }

    public void bind(Object data) {
        Finish finishData = (Finish) data;
        if (finishData != null) {
            if (finishData.getLevel() == -1) {
                this.congPlanName.setText(this.context.getString(R.string.custom_workout));
            } else {
                this.congPlanName.setText((finishData.getExType() == ExerciseType.CHALLENGE ? this.context.getString(R.string.day) : this.context.getString(R.string.level)) + " " + finishData.getLevel());
            }
            this.congPlanTime.setText(finishData.getDuration() + " " + this.context.getString(R.string.min));
        }
        this.congImg.setImageBitmap(BitmapHelper.decodeSampledBitmapFromResource(this.context.getResources(), R.drawable.congrats, BitmapHelper.getScreenWidth(this.context)));
    }

    private void initFonts(Context context) {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(context);
        this.dRegular = TypeFaceService.getInstance().getDensRegular(context);
        this.rBold = TypeFaceService.getInstance().getRobotoBold(context);
        setFonts();
    }

    private void setFonts() {
        this.congTitle.setTypeface(this.rRegular);
        this.congShare.setTypeface(this.rRegular);
        this.congAgain.setTypeface(this.rLight);
        this.congPlanName.setTypeface(this.rRegular);
        this.congPlanTime.setTypeface(this.dRegular);
    }
}
