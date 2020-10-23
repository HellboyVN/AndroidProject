package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.preview.cards;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards.AbstractCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.ResourceService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;

public class PreviewLevelCard extends AbstractCard {
    @BindView(R.id.mCardImage)
    AppCompatImageView mCardImage;
    @BindView(R.id.mCardItemLayout)
    FrameLayout mCardItemLayout;
    @BindView(R.id.mCardLayout)
    CardView mCardLayout;
    Context mContext;
    @BindView(R.id.pCardDesc)
    TextView pCardDesc;
    @BindView(R.id.pCardName)
    TextView pCardName;

    public PreviewLevelCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.preview_card_level, parent, false));
    }

    public PreviewLevelCard(Context context, View view) {
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
        this.pCardName.setText(ResourceService.getInstance().getString(currentData.getNameKey(), this.context)+ " Exercise");
        this.pCardDesc.setText(currentData.getDurationFormatted());
        this.pCardDesc.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
        int resourceId = ResourceService.getInstance().getdrawableResourceId(currentData.getImgKey(), this.context);
        if (resourceId != 0) {
            Glide.with(this.mContext).load(Integer.valueOf(resourceId)).into(this.mCardImage);
        }
    }
}
