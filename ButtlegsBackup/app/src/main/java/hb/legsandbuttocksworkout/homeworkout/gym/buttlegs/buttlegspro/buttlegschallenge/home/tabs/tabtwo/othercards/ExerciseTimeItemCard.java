package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.othercards;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabtwo.chart.cards.MoreTabAbstractCard;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Exercise;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ResourceService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ExerciseTimeItemCard extends MoreTabAbstractCard {
    private Context context;
    @BindView(R.id.mCardImage)
    AppCompatImageView mCardImage;
    @BindView(R.id.mCardItemLayout)
    FrameLayout mCardItemLayout;
    @BindView(R.id.mCardLayout)
    CardView mCardLayout;
    @BindView(R.id.pCardDesc)
    AppCompatTextView pCardDesc;
    @BindView(R.id.pCardName)
    TextView pCardName;
    Typeface rLight;
    Typeface rMedium;
    Typeface rRegular;

    public ExerciseTimeItemCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_exercise_time_item, parent, false));
        this.context = context;
    }

    public ExerciseTimeItemCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        initFonts();
    }

    public void bind(Object data) {
        Exercise currentData = (Exercise) data;
        this.pCardName.setText(ResourceService.getInstance().getString(currentData.getNameKey(), this.context)+ " Exercise");
        this.pCardDesc.setText(currentData.getDurationForStatFormatted());
        int resourceId = ResourceService.getInstance().getdrawableResourceId(currentData.getImgKey(), this.context);
        if (resourceId != 0) {
            Glide.with(this.context).load(Integer.valueOf(resourceId)).into(this.mCardImage);
        }
    }

    private void initFonts() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this.context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(this.context);
        this.rMedium = TypeFaceService.getInstance().getRobotoMedium(this.context);
        setFonts();
    }

    private void setFonts() {
        this.pCardName.setTypeface(this.rRegular);
        this.pCardDesc.setTypeface(this.rLight);
    }
}
