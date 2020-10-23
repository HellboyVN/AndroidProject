package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.othercards;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabtwo.chart.cards.MoreTabAbstractCard;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.EmailHelper;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PollCard extends MoreTabAbstractCard {
    private Context context;
    @BindView(R.id.pollDesc)
    AppCompatTextView pollAppDesc;
    @BindView(R.id.pollCard)
    CardView pollCard;
    @BindView(R.id.pollName)
    AppCompatTextView pollName;
    Typeface rLight;
    Typeface rMedium;
    Typeface rRegular;

    public PollCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_poll, parent, false));
        this.context = context;
    }

    public PollCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        initFonts();
    }

    public void bind(Object data) {
        this.pollCard.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                EmailHelper.getInstance().sendEmail("phantai9447@gmail.com", PollCard.this.context.getString(R.string.poll_title), "");
            }
        });
    }

    private void initFonts() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this.context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(this.context);
        this.rMedium = TypeFaceService.getInstance().getRobotoMedium(this.context);
        setFonts();
    }

    private void setFonts() {
        this.pollAppDesc.setTypeface(this.rRegular);
        this.pollName.setTypeface(this.rMedium);
    }
}
