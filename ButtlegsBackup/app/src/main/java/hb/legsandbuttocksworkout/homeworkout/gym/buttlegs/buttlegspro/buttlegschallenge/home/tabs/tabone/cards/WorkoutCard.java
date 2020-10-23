package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.home.tabs.tabone.cards;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.LevelData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ResourceService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;

public class WorkoutCard extends AbstractCard {
    @BindView(R.id.mCardBegin)
    TextView mCardBegin;
    @BindView(R.id.mCardDesc)
    TextView mCardDesc;
    @BindView(R.id.mCardImage)
    AppCompatImageView mCardImage;
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
        this(context, LayoutInflater.from(context).inflate(R.layout.card_main_level, parent, false));
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
        final LevelData currentData = (LevelData) data;
        String descKey = currentData.getDescKey();
        if (descKey != null) {
            this.mCardBegin.setText(String.format(ResourceService.getInstance().getString(descKey, this.context), new Object[]{ResourceService.getInstance().getString(currentData.getIntensivityLevelKey(), this.context), currentData.getDurationFormatted(),
                    ResourceService.getInstance().getString(currentData.getIntensivityLevelKey(), this.context).equals("Advanced")? "250" : "200"}));
        }
        this.mCardLevel.setText(this.mContext.getString(R.string.level) + " " + currentData.getLevel());
        this.mCardLevel.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.mCardBegin.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.mCardDesc.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
        String imgSrc = currentData.getImgSrc();
        if (imgSrc != null) {
            int resourceId = ResourceService.getInstance().getdrawableResourceId(imgSrc, this.context);
            if (resourceId != 0) {
                Glide.with(this.mContext).load(Integer.valueOf(resourceId)).into(this.mCardImage);
            }
        }
        this.mCardMask.setVisibility(8);
        this.mCardMask.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(WorkoutCard.this.mContext, WorkoutCard.this.mContext.getString(R.string.unlock_1) + String.valueOf(currentData.getLevel().intValue() - 1) + WorkoutCard.this.mContext.getString(R.string.unlock_2), 1).show();
            }
        });
    }
}
