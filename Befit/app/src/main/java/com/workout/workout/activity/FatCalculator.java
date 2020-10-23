package com.workout.workout.activity;

import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import java.text.DecimalFormat;

public class FatCalculator extends BaseActivity implements OnClickListener, OnCheckedChangeListener {
    private Button a11;
    private Button a12;
    private Button a13;
    private Button b11;
    private Button b12;
    private Button b13;
    private Button back;
    private Button c11;
    private Button c12;
    private Button c13;
    private Button calc;
    private Button d11;
    private Button d12;
    private Button d13;
    private Button e11;
    private Button e12;
    private Button e13;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private EditText et6;
    boolean isUserOnResultScreen = false;
    RelativeLayout layoutLeft;
    RelativeLayout layoutRight;
    boolean male = true;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioGroup rg;
    private TextView textViewWaist;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    ImageView waistimage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_fat);
        LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layoutLeft = (RelativeLayout) findViewById(R.id.leftlayout);
        this.layoutRight = (RelativeLayout) findViewById(R.id.rightlayout);
        LayoutParams relParam = new LayoutParams(-2, -2);
        this.layoutLeft.setVisibility(View.VISIBLE);
        this.layoutRight.setVisibility(View.GONE);
        this.rb1 = (RadioButton) findViewById(R.id.rb1);
        this.rb2 = (RadioButton) findViewById(R.id.rb2);
        this.rb1.setOnCheckedChangeListener(this);
        this.rb2.setOnCheckedChangeListener(this);
        if (FirebaseRemoteConfig.getInstance().getBoolean("bmi_calculator_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_FAT_BANNER_AD_ID);
        }
        loadInterstitialAds(AppConstants.ADMOB_FAT_INTERSTITIAL_AD_ID);
        initializeView();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeView() {
        setToolbar("Fat Calculator", true);
        this.waistimage = (ImageView) findViewById(R.id.waistid);
        this.textViewWaist = (TextView) findViewById(R.id.textViewWaist);
        this.a11 = (Button) findViewById(R.id.a11);
        this.a12 = (Button) findViewById(R.id.a12);
        this.a13 = (Button) findViewById(R.id.a13);
        this.b11 = (Button) findViewById(R.id.b11);
        this.b12 = (Button) findViewById(R.id.b12);
        this.b13 = (Button) findViewById(R.id.b13);
        this.c11 = (Button) findViewById(R.id.c11);
        this.c12 = (Button) findViewById(R.id.c12);
        this.c13 = (Button) findViewById(R.id.c13);
        this.d11 = (Button) findViewById(R.id.d11);
        this.d12 = (Button) findViewById(R.id.d12);
        this.d13 = (Button) findViewById(R.id.d13);
        this.e11 = (Button) findViewById(R.id.e11);
        this.e12 = (Button) findViewById(R.id.e12);
        this.e13 = (Button) findViewById(R.id.e13);
        this.calc = (Button) findViewById(R.id.buttonCalculateFat);
        this.et1 = (EditText) findViewById(R.id.et1);
        this.et2 = (EditText) findViewById(R.id.et2);
        this.et3 = (EditText) findViewById(R.id.et3);
        this.et4 = (EditText) findViewById(R.id.et4);
        this.et5 = (EditText) findViewById(R.id.et5);
        this.et6 = (EditText) findViewById(R.id.et6);
        this.rb1 = (RadioButton) findViewById(R.id.rb1);
        this.rb2 = (RadioButton) findViewById(R.id.rb2);
        this.tv1 = (TextView) findViewById(R.id.tv1);
        this.tv2 = (TextView) findViewById(R.id.tv2);
        this.tv3 = (TextView) findViewById(R.id.tv3);
        this.calc.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCalculateFat:
                calculateAndSetViews();
                return;
            default:
                return;
        }
    }

    private void calculateAndSetViews() {
        String s1 = this.et1.getText().toString();
        String s2 = this.et2.getText().toString();
        String s3 = this.et3.getText().toString();
        String s4 = this.et4.getText().toString();
        String s5 = this.et5.getText().toString();
        String s6 = this.et6.getText().toString();
        if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty() || s4.isEmpty() || s5.isEmpty() || s6.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        double weight = (double) Integer.parseInt(s2);
        double height = (double) Integer.parseInt(s3);
        double waist = (double) Integer.parseInt(s4);
        double hip = (double) Integer.parseInt(s5);
        double neck = (double) Integer.parseInt(s6);
        if (((double) Integer.parseInt(s1)) == 0.0d || weight == 0.0d || height == 0.0d || waist == 0.0d || hip == 0.0d || neck == 0.0d) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        double fat = 0.0d;
        double fatmass = 0.0d;
        double leanmass = 0.0d;
        if (this.male) {
            try {
                fat = (495.0d / ((1.0324d - (0.19077d * Math.log10(waist - neck))) + (0.15456d * Math.log10(height)))) - 450.0d;
                fatmass = (fat * weight) / 100.0d;
                leanmass = weight - fatmass;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "error! try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            try {
                fat = (495.0d / ((1.29579d - (0.35004d * Math.log10((waist + hip) - neck))) + (0.221d * Math.log10(height)))) - 450.0d;
                fatmass = (fat * weight) / 100.0d;
                leanmass = weight - fatmass;
            } catch (Exception e2) {
                e2.printStackTrace();
                Toast.makeText(this, "error! try again", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            Answers.getInstance().logContentView((ContentViewEvent) ((ContentViewEvent) ((ContentViewEvent) new ContentViewEvent().putContentName("Fat Calculated").putCustomAttribute("fat value", Double.valueOf(fat))).putCustomAttribute("fatmass", Double.valueOf(fatmass))).putCustomAttribute("leanmass", Double.valueOf(leanmass)));
        } catch (Exception e22) {
            e22.printStackTrace();
        }
        if (fat <= 1.0d || fat >= 100.0d || fatmass <= 0.0d || leanmass <= 0.0d) {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show();
            return;
        }
        this.layoutLeft.setVisibility(View.GONE);
        this.layoutRight.setVisibility(View.VISIBLE);
        this.isUserOnResultScreen = true;
        this.a11.setBackgroundResource(R.drawable.bmi_button_background);
        this.a12.setBackgroundResource(R.drawable.bmi_button_background);
        this.a13.setBackgroundResource(R.drawable.bmi_button_background);
        this.b11.setBackgroundResource(R.drawable.bmi_button_background);
        this.b12.setBackgroundResource(R.drawable.bmi_button_background);
        this.b13.setBackgroundResource(R.drawable.bmi_button_background);
        this.c11.setBackgroundResource(R.drawable.bmi_button_background);
        this.c12.setBackgroundResource(R.drawable.bmi_button_background);
        this.c13.setBackgroundResource(R.drawable.bmi_button_background);
        this.d11.setBackgroundResource(R.drawable.bmi_button_background);
        this.d12.setBackgroundResource(R.drawable.bmi_button_background);
        this.d13.setBackgroundResource(R.drawable.bmi_button_background);
        this.e11.setBackgroundResource(R.drawable.bmi_button_background);
        this.e12.setBackgroundResource(R.drawable.bmi_button_background);
        this.e13.setBackgroundResource(R.drawable.bmi_button_background);
        if (this.male) {
            if (fat >= 2.0d && fat < 5.0d) {
                this.a11.setBackgroundColor(-16711936);
                this.a12.setBackgroundColor(-16711936);
                this.a13.setBackgroundColor(-16711936);
            } else if (fat >= 6.0d && fat < 14.0d) {
                this.b11.setBackgroundColor(-16711936);
                this.b12.setBackgroundColor(-16711936);
                this.b13.setBackgroundColor(-16711936);
            } else if (fat >= 14.0d && fat < 18.0d) {
                this.c11.setBackgroundColor(-16711936);
                this.c12.setBackgroundColor(-16711936);
                this.c13.setBackgroundColor(-16711936);
            } else if (fat >= 18.0d && fat < 25.0d) {
                this.d11.setBackgroundColor(-16711936);
                this.d12.setBackgroundColor(-16711936);
                this.d13.setBackgroundColor(-16711936);
            } else if (fat > 25.0d) {
                this.e11.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                this.e12.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                this.e13.setBackgroundColor(SupportMenu.CATEGORY_MASK);
            }
        } else if (fat >= 10.0d && fat < 14.0d) {
            this.a11.setBackgroundColor(-16711936);
            this.a12.setBackgroundColor(-16711936);
            this.a13.setBackgroundColor(-16711936);
        } else if (fat >= 14.0d && fat < 21.0d) {
            this.b11.setBackgroundColor(-16711936);
            this.b12.setBackgroundColor(-16711936);
            this.b13.setBackgroundColor(-16711936);
        } else if (fat >= 21.0d && fat < 25.0d) {
            this.c11.setBackgroundColor(-16711936);
            this.c12.setBackgroundColor(-16711936);
            this.c13.setBackgroundColor(-16711936);
        } else if (fat >= 25.0d && fat < 32.0d) {
            this.d11.setBackgroundColor(-16711936);
            this.d12.setBackgroundColor(-16711936);
            this.d13.setBackgroundColor(-16711936);
        } else if (fat > 32.0d) {
            this.e11.setBackgroundColor(SupportMenu.CATEGORY_MASK);
            this.e12.setBackgroundColor(SupportMenu.CATEGORY_MASK);
            this.e13.setBackgroundColor(SupportMenu.CATEGORY_MASK);
        }
        this.tv1 = (TextView) findViewById(R.id.tv1);
        this.tv2 = (TextView) findViewById(R.id.tv2);
        this.tv3 = (TextView) findViewById(R.id.tv3);
        this.tv1.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.tv2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.tv3.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.tv1.setText(new DecimalFormat("0.00").format(fat) + " %");
        this.tv2.setText(new DecimalFormat("0.00").format(fatmass) + " Kg");
        this.tv3.setText(new DecimalFormat("0.00").format(leanmass) + " Kg");
    }

    public void onBackPressed() {
        if (this.isUserOnResultScreen) {
            boolean showInterstitialAd;
            if (((long) PersistenceManager.getBMICalculateCount()) % ((long) PersistenceManager.getAdmob_bmi_interstitial_ads_interval()) == 0) {
                showInterstitialAd = true;
            } else {
                showInterstitialAd = false;
            }
            if (this.interstitialAd != null && this.interstitialAd.isLoaded() && showInterstitialAd) {
                showInterstitialAds(AppConstants.ADMOB_FAT_INTERSTITIAL_AD_ID);
                this.interstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        FatCalculator.this.loadInterstitialAds(AppConstants.ADMOB_FAT_INTERSTITIAL_AD_ID);
                        FatCalculator.this.handleFatBack();
                    }
                });
                return;
            }
            handleFatBack();
            return;
        }
        super.onBackPressed();
    }

    private void handleFatBack() {
        this.layoutLeft.setVisibility(View.VISIBLE);
        this.layoutRight.setVisibility(View.GONE);
        this.isUserOnResultScreen = false;
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rb1:
                if (b) {
                    this.male = true;
                    this.waistimage.setImageResource(R.drawable.abds);
                    this.textViewWaist.setText("Abdomen");
                    return;
                }
                return;
            case R.id.rb2:
                if (b) {
                    this.male = false;
                    this.textViewWaist.setText("Waist");
                    this.waistimage.setImageResource(R.drawable.waist);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
