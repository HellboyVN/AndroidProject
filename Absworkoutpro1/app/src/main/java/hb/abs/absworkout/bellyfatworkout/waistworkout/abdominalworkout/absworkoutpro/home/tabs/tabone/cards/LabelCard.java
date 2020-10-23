package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabone.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.LevelData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.ResourceService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LabelCard extends AbstractCard {
    @BindView(R.id.mLabelTitle)
    TextView mLabelTitle;

    public LabelCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_main_label, parent, false));
    }

    public LabelCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
    }

    public void bind(Object data) {
        this.mLabelTitle.setText(ResourceService.getInstance().getString(((LevelData) data).getNameKey(), this.context));
        this.mLabelTitle.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
    }
}
