package hb.me.instagramsave.Utils;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.util.ArrayList;
import java.util.List;

import hb.me.instagramsave.Activities.MainActivity;
import hb.me.instagramsave.Intro.IntroActivity;
import hb.me.instagramsave.R;

import static hb.me.instagramsave.R.id.adView;

public class SettingsActivity extends AppCompatActivity {
    int appColor;
    int appTheme;
    SharedPreferences app_preferences;
    Button button;
    Constant constant;
    Editor editor;
    LinearLayout llHelp;
    LinearLayout llprivacyPolicy;
    Methods methods;
    SharedPreferences sharedPreferences;
    int themeColor;
    private NativeAd nativeAd;
    private LinearLayout nativeAdContainer;
    private LinearLayout  adView;
    LinearLayout adsView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constant.showFAd(this);
        this.app_preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.appColor = this.app_preferences.getInt("color", 0);
        this.appTheme = this.app_preferences.getInt("theme", 0);
        this.themeColor = this.appColor;
        Constant constant = this.constant;
        Constant.color = this.appColor;
        if (this.themeColor == 0) {
            setTheme(Constant.theme);
        } else if (this.appTheme == 0) {
            setTheme(Constant.theme);
        } else {
            setTheme(this.appTheme);
        }
        setContentView((int) R.layout.activity_settings);
        adsView =(LinearLayout)findViewById(R.id.native_ad_admob_1);
        showNativeAd();
        this.llHelp = (LinearLayout) findViewById(R.id.llhelp);
        this.llprivacyPolicy = (LinearLayout) findViewById(R.id.llprivacypolicy);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        toolbar.setTitle((CharSequence) "Settings");
        toolbar.setBackgroundColor(Constant.color);
        toolbar.setTitleTextColor(-1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.methods = new Methods();
        this.button = (Button) findViewById(R.id.button_color);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = this.sharedPreferences.edit();
        colorize();
        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ColorChooserDialog dialog = new ColorChooserDialog(SettingsActivity.this);
                dialog.setTitle("Select");
                dialog.setColorListener(new ColorListener() {
                    public void OnColorClick(View v, int color) {
                        SettingsActivity.this.colorize();
                        Constant.color = color;
                        SettingsActivity.this.methods.setColorTheme();
                        SettingsActivity.this.editor.putInt("color", Constant.color);
                        SettingsActivity.this.editor.putInt("theme", Constant.theme);
                        SettingsActivity.this.editor.commit();
                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        intent.setFlags(67108864);
                        SettingsActivity.this.startActivity(intent);
                    }
                });
                dialog.show();
            }
        });
        this.llHelp.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, IntroActivity.class));
            }
        });
        this.llprivacyPolicy.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TextView textView = (TextView) new Builder(SettingsActivity.this).setTitle((CharSequence) "PRIVACY POLICY").setMessage((int) R.string.privacy_message).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setIcon((int) R.drawable.ic_info_black_24dp).show().findViewById(android.R.id.message);
                textView.setScroller(new Scroller(SettingsActivity.this));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(16)
    private void colorize() {
        ShapeDrawable d = new ShapeDrawable(new OvalShape());
        d.setBounds(58, 58, 58, 58);
        d.getPaint().setStyle(Style.FILL);
        d.getPaint().setColor(Constant.color);
        this.button.setBackground(d);
    }
    private void showNativeAd(){
        Log.e("NativeADS","----HERE----");
        nativeAd = new NativeAd(this, "2013570418887999_2013593535552354");
        nativeAd.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                Log.e("NativeADSLoaded","----HERE----");
                adsView.setVisibility(View.VISIBLE);
                if (nativeAd != null) {
                    nativeAd.unregisterView();
                }

                // Add the Ad view into the ad container.
                nativeAdContainer = (LinearLayout) findViewById(R.id.native_ad_admob_1);
                LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, nativeAdContainer, false);
                nativeAdContainer.addView(adView);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adView.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adView.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = (MediaView) adView.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adView.findViewById(R.id.native_ad_social_context);
                TextView nativeAdBody = (TextView) adView.findViewById(R.id.native_ad_body);
                Button nativeAdCallToAction = (Button) adView.findViewById(R.id.native_ad_call_to_action);

                // Set the Text.
                nativeAdTitle.setText(nativeAd.getAdTitle());
                nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                nativeAdBody.setText(nativeAd.getAdBody());
                nativeAdCallToAction.setText(nativeAd.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = nativeAd.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and display the cover image.
                nativeAdMedia.setNativeAd(nativeAd);

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) findViewById(R.id.ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(SettingsActivity.this, nativeAd, true);
                adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                nativeAd.registerViewForInteraction(nativeAdContainer,clickableViews);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

        nativeAd.loadAd(NativeAd.MediaCacheFlag.ALL);
    }
}
