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
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderCard extends AbstractCard {
    Context mContext;
    @BindView(R.id.rCardImage)
    AppCompatImageView rCardImage;
    @BindView(R.id.rCardItemLayout)
    FrameLayout rCardItemLayout;
    @BindView(R.id.rCardLayout)
    CardView rCardLayout;
    @BindView(R.id.rCardSetReminder)
    TextView rCardSetReminder;
    @BindView(R.id.rCardTitle)
    TextView rCardTitle;

    public ReminderCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_reminder, parent, false));
    }

    public ReminderCard(Context context, View view) {
        super(view, context);
        this.mContext = context;
        try {
            ButterKnife.bind((Object) this, view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(Object data) {
        this.rCardTitle.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        this.rCardSetReminder.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
    }
}
