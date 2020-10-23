package hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.GlideHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class PreviewExerciseCard extends AbstractCard {
    @BindView(R.id.mCardImage)
    ImageView mCardImage;
    @BindView(R.id.mCardItemLayout)
    FrameLayout mCardItemLayout;
    @BindView(R.id.mCardLayout)
    CardView mCardLayout;
    Context mContext;
    @BindView(R.id.pCardDesc)
    TextView pCardDesc;
    @BindView(R.id.pCardName)
    TextView pCardName;

    public PreviewExerciseCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.preview_card_level, parent, false));
    }

    public PreviewExerciseCard(Context context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
        Exercise currentData = (Exercise) data;
        this.pCardName.setText(ResourceService.getInstance().getString(currentData.getNameKey(), this.context));
        StringBuilder descBuilder = new StringBuilder();
        descBuilder.append(currentData.getDurationFormatted());
        int repetition = currentData.getRepetition();
        if (repetition > 1) {
            descBuilder.append(" (x");
            descBuilder.append(String.valueOf(repetition));
            descBuilder.append(")");
        }
        this.pCardDesc.setText(descBuilder.toString() + " " + this.context.getString(R.string.seconds));
        this.pCardDesc.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
        int resourceId = ResourceService.getInstance().getdrawableResourceId(currentData.getImgKey(), this.context);
        if (resourceId != 0) {
            GlideHelper.loadResource(this.mContext, this.mCardImage, resourceId);
        }
    }
}
