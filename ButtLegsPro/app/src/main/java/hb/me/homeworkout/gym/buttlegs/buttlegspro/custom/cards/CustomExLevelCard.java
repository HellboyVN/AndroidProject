package hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.cards;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.CustomPreviewActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.IDurationChange;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SnackBarService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class CustomExLevelCard extends CustomExAbstractCard {
    @BindView(R.id.buyButtonsLayout)
    LinearLayout buyButtonsLayout;
    @BindView(R.id.cAddTime)
    AppCompatImageView cAddTime;
    @BindView(R.id.cCardImage)
    ImageView cCardImage;
    @BindView(R.id.cCardTitele)
    TextView cCardTitele;
    @BindView(R.id.cRemoveTime)
    AppCompatImageView cRemoveTime;
    IDurationChange listener;
    @BindView(R.id.mCardItemLayout)
    FrameLayout mCardItemLayout;
    @BindView(R.id.mCardLayout)
    CardView mCardLayout;
    Context mContext;
    @BindView(R.id.productAddToCardText)
    TextView productAddToCardText;

    public CustomExLevelCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.preview_custom_level, parent, false));
    }

    public CustomExLevelCard(Context context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDurationChangeListener(IDurationChange listener) {
        this.listener = listener;
    }

    public void bind(Object data) {
        final Exercise currentData = (Exercise) data;
        this.cCardTitele.setText(ResourceService.getInstance().getString(currentData.getNameKey(), this.context));
        this.cCardTitele.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        int resourceId = ResourceService.getInstance().getdrawableResourceId(currentData.getImgKey(), this.context);
        if (resourceId != 0) {
            Glide.with(this.mContext).load(Integer.valueOf(resourceId)).into(this.cCardImage);
        }
        this.productAddToCardText.setText(String.valueOf(currentData.getDuration().intValue() / 1000) + this.mContext.getString(R.string.seconds_short));
        this.cRemoveTime.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (currentData.getDuration().intValue() > 0) {
                    currentData.setDuration(Integer.valueOf(currentData.getDuration().intValue() - 10000));
                    CustomExLevelCard.this.productAddToCardText.setText(String.valueOf(currentData.getDuration().intValue() / 1000) + CustomExLevelCard.this.mContext.getString(R.string.seconds_short));
                    CustomExLevelCard.this.listener.onChange(-10000);
                }
            }
        });
        this.cAddTime.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (currentData.getDuration().intValue() == 0 && ((CustomPreviewActivity) CustomExLevelCard.this.listener).filterExercise().size() >= 12) {
                    SnackBarService.showQuickSnackBar((Activity) CustomExLevelCard.this.context, CustomExLevelCard.this.context.getString(R.string.custom_workout_max, new Object[]{Integer.valueOf(12)}));
                } else if (currentData.getDuration().intValue() < 180000) {
                    currentData.setDuration(Integer.valueOf(currentData.getDuration().intValue() + 10000));
                    CustomExLevelCard.this.productAddToCardText.setText(String.valueOf(currentData.getDuration().intValue() / 1000) + CustomExLevelCard.this.mContext.getString(R.string.seconds_short));
                    CustomExLevelCard.this.listener.onChange(10000);
                } else {
                    Toast.makeText(CustomExLevelCard.this.context, "Choose another exercise", 0).show();
                }
            }
        });
    }
}
