package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class LabelCard extends AbstractCard {
    @BindView(R.id.mLabelTitle)
    TextView mLabelTitle;

    public LabelCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.main_card_label, parent, false));
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
