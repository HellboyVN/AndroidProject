package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.preview.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.Exercise;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ResourceService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewLabelCard extends PreviewAbstractCard {
    @BindView(R.id.mLabelTitle)
    TextView mLabelTitle;

    public PreviewLabelCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_main_label, parent, false));
    }

    public PreviewLabelCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
    }

    public void bind(Object data) {
        this.mLabelTitle.setText(ResourceService.getInstance().getString(((Exercise) data).getNameKey(), this.context));
        this.mLabelTitle.setTypeface(TypeFaceService.getInstance().getRobotoLight(this.context));
    }
}
