package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.cards;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.ads.INativeAd;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.google.android.gms.ads.VideoController;

public class AdCard extends AbstractCard {
    @BindView(R.id.adButtonsLayout)
    LinearLayout adButtonsLayout;
    @BindView( R.id.adLayout)
    LinearLayout adLayout;
    private Context context;
    private VideoController mVideoController;
    ViewGroup parent;
    @BindView(R.id.removeAds)
    AppCompatTextView removeAds;
    @BindView(R.id.whyAds)
    AppCompatTextView whyAds;

    public AdCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.ad_card, parent, false));
        this.context = context;
        this.parent = parent;
    }

    public AdCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
    }

    public void bind(Object data) {
        this.adButtonsLayout.setVisibility(SharedPrefsService.getInstance().getPurchaseAvailable(this.context) ? 0 : 8);
        this.removeAds.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AdCard.this.removeAdClicked(AdCard.this.getAdapterPosition());
            }
        });
        this.whyAds.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AdCard.this.whyAdsClicked();
            }
        });
        this.removeAds.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this.context));
        LinearLayout adView = (LinearLayout) ((INativeAd) data).getAdView();
        if (this.adLayout.getChildCount() > 0) {
            this.adLayout.removeAllViews();
        }
        if (adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }
        this.adLayout.addView(adView);
    }
}
