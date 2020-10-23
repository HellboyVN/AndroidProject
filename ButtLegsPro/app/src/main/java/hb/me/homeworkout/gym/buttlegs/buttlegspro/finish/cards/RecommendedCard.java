package hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.model.Recommended;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.cards.AbstractCard;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RateHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

public class RecommendedCard extends AbstractCard {
    private Context context;
    Typeface rLight;
    Typeface rMedium;
    Typeface rRegular;
    @BindView(R.id.recAppName)
    AppCompatTextView recAppName;
    @BindView(R.id.recCard)
    CardView recCard;
    @BindView(R.id.recImage)
    ImageView recImage;
    @BindView(R.id.recTitle)
    AppCompatTextView recTitle;

    public RecommendedCard(Context context, ViewGroup parent) {
        this(context, LayoutInflater.from(context).inflate(R.layout.card_recommended, parent, false));
        this.context = context;
    }

    public RecommendedCard(Context context, View view) {
        super(view, context);
        ButterKnife.bind((Object) this, view);
        initFonts(context);
    }

    public void bind(Object data) {
        final Recommended recommended = new Recommended();
        recommended.setAppName(this.context.getString(R.string.rec_app_abs_name));
        recommended.setImgKey("abs_fmale_recomend");
        recommended.setAppPackage(ConsKeys.RECOMMENDED_APP_ABS_WOMEN);
        this.recAppName.setText(recommended.getAppName());
        int resourceId = ResourceService.getInstance().getdrawableResourceId(recommended.getImgKey(), this.context);
        if (resourceId != 0) {
            Glide.with(this.context).load(Integer.valueOf(resourceId)).into(this.recImage);
        }
        this.recCard.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RecommendedCard.this.open(recommended.getAppPackage(), RecommendedCard.this.context.getString(R.string.get_stronger));
                Bundle bundle = new Bundle();
                bundle.putString("item_name", "recommend_card_clicked");
            }
        });
    }

    private void open(String packageName, String message) {
        if (RateHelper.appInstalledOrNot(this.context, packageName)) {
            Intent launchIntent = this.context.getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                this.context.startActivity(launchIntent);
                return;
            }
            return;
        }
        RateHelper.openPlayStore(this.context, packageName, message);
    }

    private void initFonts(Context context) {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(context);
        this.rMedium = TypeFaceService.getInstance().getRobotoMedium(context);
        setFonts();
    }

    private void setFonts() {
        this.recTitle.setTypeface(this.rMedium);
        this.recAppName.setTypeface(this.rRegular);
    }
}
