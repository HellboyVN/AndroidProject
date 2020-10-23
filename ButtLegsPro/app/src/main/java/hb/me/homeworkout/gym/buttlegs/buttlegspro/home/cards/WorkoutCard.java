package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class WorkoutCard extends AbstractCard {
    @BindView(R.id.mCardBegin)
    TextView mCardBegin;
    @BindView(R.id.mCardDesc)
    TextView mCardDesc;
    @BindView(R.id.mCardImage)
    ImageView mCardImage;
    @BindView(R.id.mCardItemLayout)
    FrameLayout mCardItemLayout;
    @BindView(R.id.mCardLayout)
    CardView mCardLayout;
    @BindView(R.id.mCardLevel)
    TextView mCardLevel;
    @BindView(R.id.mCardMask)
    FrameLayout mCardMask;
    Context mContext;

    public WorkoutCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.main_card_workout, parent, false));
    }

    public WorkoutCard(Context context, View view) {
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
        this.mCardDesc.setText(String.format(ResourceService.getInstance().getString(currentData.getDescKey(), this.mContext), new Object[]{ResourceService.getInstance().getString(currentData.getIntensivityLevelKey(), this.mContext), currentData.getDurationFormatted()}));
        this.mCardDesc.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
        this.mCardLevel.setText(this.mContext.getString(R.string.level) + " " + currentData.getLevel());
        this.mCardLevel.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.mCardBegin.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        int resourceId = ResourceService.getInstance().getdrawableResourceId(currentData.getImgSrc(), this.mContext);
        if (resourceId != 0) {
            Glide.with(this.mContext).load(Integer.valueOf(resourceId)).into(this.mCardImage);
        }
        this.mCardItemLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.mCardImage.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.mCardMask.setVisibility(8);
    }
}
