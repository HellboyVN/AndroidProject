package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.cards;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.model.Recommended;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabone.cards.AbstractCard;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ConsKeys;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.RateHelper;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ResourceService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    AppCompatImageView recImage;
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
        switch (SharedPrefsService.getInstance().getGender(this.context)) {
            case 2:
                recommended.setAppName(this.context.getString(R.string.rec_app_but_name));
                recommended.setImgKey("ic_lose_weight");
                recommended.setAppPackage(ConsKeys.RECOMMENDED_APP_BUTT);
                break;
            default:
                recommended.setAppName(this.context.getString(R.string.rec_app_push_abs_name));
                recommended.setImgKey("ic_pushups");
                recommended.setAppPackage(ConsKeys.RECOMMENDED_APP_PUSH_UPS);
                break;
        }
        this.recAppName.setText(recommended.getAppName());
        int resourceId = ResourceService.getInstance().getdrawableResourceId(recommended.getImgKey(), this.context);
        if (resourceId != 0) {
            Glide.with(this.context).load(Integer.valueOf(resourceId)).into(this.recImage);
        }
        this.recCard.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RecommendedCard.this.open(recommended.getAppPackage(), RecommendedCard.this.context.getString(R.string.get_stronger));
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
        SharedPrefsService.getInstance().setRecommendedStatus(this.context, true);
    }

    private void initFonts(Context context) {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(context);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(context);
        this.rMedium = TypeFaceService.getInstance().getRobotoMedium(context);
        setFonts();
    }

    private void setFonts() {
        this.recTitle.setTypeface(this.rRegular);
        this.recAppName.setTypeface(this.rRegular);
    }
}
