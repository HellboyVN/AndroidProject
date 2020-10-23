package hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards;

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
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class ReminderCard extends AbstractCard {
    Context mContext;
    @BindView(R.id.rCardImage)
    ImageView rCardImage;
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
