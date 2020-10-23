package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.LevelData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomWorkoutCard extends AbstractCard {
    @BindView(R.id.customCardDesc)
    TextView customCardDesc;
    @BindView(R.id.customCardImage)
    AppCompatImageView customCardImage;
    @BindView(R.id.customCardItemLayout)
    FrameLayout customCardItemLayout;
    @BindView(R.id.customCardLayout)
    CardView customCardLayout;
    @BindView(R.id.customCardTitle)
    TextView customCardTitle;
    @BindView(R.id.mCardBegin)
    TextView mCardBegin;
    @BindView(R.id.mCardMask)
    FrameLayout mCardMask;
    Context mContext;

    public CustomWorkoutCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_custom_workout, parent, false));
    }

    public CustomWorkoutCard(Context context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
        LevelData currentData = (LevelData) data;
        this.customCardDesc.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
        this.customCardTitle.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.mCardBegin.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
    }
}
