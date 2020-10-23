package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.preview.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Exercise;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ResourceService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.TypeFaceService;
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
