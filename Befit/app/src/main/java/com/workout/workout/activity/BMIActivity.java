package com.workout.workout.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.internal.view.SupportMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.util.AppUtil;

public class BMIActivity extends BaseActivity implements OnCheckedChangeListener, OnClickListener {
    private Button button1OverWeight;
    private Button buttonBack;
    private Button buttonCalculate;
    private Button buttonCategory;
    private Button buttonHeader;
    private Button buttonLessThan16;
    private Button buttonNormal;
    private Button buttonObese;
    private Button buttonRange;
    private Button buttonRangeNormal;
    private Button buttonRangeObese;
    private Button buttonRangeOverWeight;
    private Button buttonRangeUnderWeight;
    private Button buttonSevereUnderWeight;
    private Button buttonUnderWeight;
    private EditText editTextHeight;
    private EditText editTextHeightSecond;
    private EditText editTextWeight;
    private boolean isUserOnResultScreen;
    private ImageView iv1;
    private ImageView iv2;
    private LinearLayout linearLayoutBMITable;
    private LinearLayout linearLayoutVal;
    private RadioButton radioButtonImperial;
    private RadioButton radioButtonMetric;
    private RadioGroup rg;
    private TextView textViewHeight;
    private TextView textViewResult;
    private TextView textViewSelectUnit;
    private TextView textViewTitle;
    private TextView textViewWeight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_bmi);
        if (FirebaseRemoteConfig.getInstance().getBoolean("bmi_calculator_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_BMI_BANNER_AD_ID);
        }
        loadInterstitialAds(AppConstants.ADMOB_BMI_INTERSTITIAL_AD_ID);
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
        setToolbar("BMI Calculator", true);
        this.linearLayoutBMITable = (LinearLayout) findViewById(R.id.linearLayoutBMITable);
        this.linearLayoutVal = (LinearLayout) findViewById(R.id.linearLayoutVal);
        this.buttonHeader = (Button) findViewById(R.id.buttonHeader);
        this.buttonCategory = (Button) findViewById(R.id.buttonCategory);
        this.buttonRange = (Button) findViewById(R.id.buttonRange);
        this.buttonLessThan16 = (Button) findViewById(R.id.buttonLessThan16);
        this.buttonSevereUnderWeight = (Button) findViewById(R.id.buttonSevereUnderWeight);
        this.buttonRangeUnderWeight = (Button) findViewById(R.id.buttonRangeUnderWeight);
        this.buttonUnderWeight = (Button) findViewById(R.id.buttonUnderWeight);
        this.buttonRangeNormal = (Button) findViewById(R.id.buttonRangeNormal);
        this.buttonNormal = (Button) findViewById(R.id.buttonNormal);
        this.buttonRangeOverWeight = (Button) findViewById(R.id.buttonRangeOverWeight);
        this.button1OverWeight = (Button) findViewById(R.id.button1OverWeight);
        this.buttonRangeObese = (Button) findViewById(R.id.buttonRangeObese);
        this.buttonObese = (Button) findViewById(R.id.buttonObese);
        this.buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        this.buttonBack = (Button) findViewById(R.id.buttonBack);
        this.editTextHeight = (EditText) findViewById(R.id.editTextHeight);
        this.editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        this.editTextHeightSecond = (EditText) findViewById(R.id.editTextHeightSecond);
        this.radioButtonMetric = (RadioButton) findViewById(R.id.radioButtonMetric);
        this.radioButtonImperial = (RadioButton) findViewById(R.id.radioButtonImperial);
        this.textViewResult = (TextView) findViewById(R.id.textViewResult);
        this.textViewHeight = (TextView) findViewById(R.id.textViewHeight);
        this.textViewWeight = (TextView) findViewById(R.id.textViewWeight);
        this.textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        this.textViewSelectUnit = (TextView) findViewById(R.id.textViewSelectUnit);
        this.linearLayoutBMITable.setVisibility(View.GONE);
        this.buttonBack.setVisibility(View.GONE);
        this.buttonCalculate.setOnClickListener(this);
        this.buttonBack.setOnClickListener(this);
        this.radioButtonMetric.setOnCheckedChangeListener(this);
        this.radioButtonImperial.setOnCheckedChangeListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCalculate:
                calculateBMI();
                return;
            case R.id.buttonBack:
                boolean showInterstitialAd;
                if (((long) PersistenceManager.getBMICalculateCount()) % ((long) PersistenceManager.getAdmob_bmi_interstitial_ads_interval()) == 0) {
                    showInterstitialAd = true;
                } else {
                    showInterstitialAd = false;
                }
                if (this.interstitialAd != null && this.interstitialAd.isLoaded() && showInterstitialAd) {
                    showInterstitialAds(AppConstants.ADMOB_BMI_INTERSTITIAL_AD_ID);
                    this.interstitialAd.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            super.onAdClosed();
                            BMIActivity.this.loadInterstitialAds(AppConstants.ADMOB_BMI_INTERSTITIAL_AD_ID);
                            BMIActivity.this.handleBMIBack();
                        }
                    });
                    return;
                }
                handleBMIBack();
                return;
            default:
                return;
        }
    }

    private void handleBMIBack() {
        this.linearLayoutBMITable.setVisibility(View.GONE);
        this.linearLayoutVal.setVisibility(View.VISIBLE);
        this.buttonBack.setVisibility(View.GONE);
        this.textViewResult.setVisibility(View.GONE);
        this.textViewTitle.setVisibility(View.VISIBLE);
        this.textViewSelectUnit.setVisibility(View.VISIBLE);
        this.isUserOnResultScreen = false;
    }

    private void calculateBMI() {
        AppUtil.hideKeyboard(this);
        String weightString = this.editTextWeight.getText().toString();
        String heightString = this.editTextHeight.getText().toString();
        if (weightString.contentEquals(".") || heightString.contentEquals(".")) {
            Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
        } else if (weightString.contentEquals("") || heightString.contentEquals("")) {
            this.isUserOnResultScreen = false;
            this.textViewResult.setVisibility(View.GONE);
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            this.linearLayoutBMITable.setVisibility(View.GONE);
            this.linearLayoutVal.setVisibility(View.VISIBLE);
            this.buttonBack.setVisibility(View.GONE);
            this.textViewTitle.setVisibility(View.VISIBLE);
            this.textViewSelectUnit.setVisibility(View.VISIBLE);
        } else {
            float bmi;
            float height = Float.valueOf(heightString).floatValue();
            float weight = Float.valueOf(weightString).floatValue();
            height *= height;
            this.isUserOnResultScreen = true;
            if (this.radioButtonMetric.isChecked()) {
                bmi = weight / (height / 10000.0f);
                if (!(weightString.contentEquals("") || heightString.contentEquals("") || weightString.contentEquals(".") || heightString.contentEquals("."))) {
                    try {
                        Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("BMI Calculated").putCustomAttribute("BMI value", Float.valueOf(bmi)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    this.textViewResult.setText(String.valueOf("Your BMI score : " + bmi));
                    this.linearLayoutBMITable.setVisibility(View.VISIBLE);
                    this.linearLayoutVal.setVisibility(View.GONE);
                    this.buttonBack.setVisibility(View.VISIBLE);
                    this.textViewResult.setVisibility(View.VISIBLE);
                    this.textViewTitle.setVisibility(View.GONE);
                    this.textViewSelectUnit.setVisibility(View.GONE);
                    if (bmi < 16.0f) {
                        this.buttonSevereUnderWeight.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.buttonLessThan16.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.textViewResult.setTextColor(SupportMenu.CATEGORY_MASK);
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (bmi >= 16.0f && ((double) bmi) <= 18.5d) {
                        this.buttonRangeUnderWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.buttonUnderWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.textViewResult.setTextColor(Color.parseColor("#ffc04c"));
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (((double) bmi) > 18.5d && bmi <= 25.0f) {
                        this.buttonRangeNormal.setBackgroundColor(-16711936);
                        this.buttonNormal.setBackgroundColor(-16711936);
                        this.textViewResult.setTextColor(-16711936);
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (bmi > 25.0f && bmi <= 30.0f) {
                        this.buttonRangeOverWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.button1OverWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.textViewResult.setTextColor(Color.parseColor("#ffc04c"));
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (bmi > 30.0f) {
                        this.buttonRangeObese.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.buttonObese.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.textViewResult.setTextColor(SupportMenu.CATEGORY_MASK);
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                }
            }
            if (this.radioButtonImperial.isChecked()) {
                String heightFeet = this.editTextHeight.getText().toString();
                String heightInch = this.editTextHeightSecond.getText().toString();
                if (AppUtil.isEmpty(heightFeet)) {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (AppUtil.isEmpty(heightInch)) {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (heightFeet.contentEquals(".")) {
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                } else if (heightInch.contentEquals(".")) {
                    Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                } else {
                    float feet = Float.valueOf(heightFeet).floatValue();
                    float totalHeight = (12.0f * feet) + Float.valueOf(heightInch).floatValue();
                    bmi = (703.0f * weight) / (totalHeight * totalHeight);
                    this.textViewResult.setText(String.valueOf("Your BMI score : " + bmi));
                    this.linearLayoutBMITable.setVisibility(View.VISIBLE);
                    this.linearLayoutVal.setVisibility(View.GONE);
                    this.buttonBack.setVisibility(View.VISIBLE);
                    this.textViewResult.setVisibility(View.VISIBLE);
                    this.textViewTitle.setVisibility(View.GONE);
                    this.textViewSelectUnit.setVisibility(View.GONE);
                    if (bmi < 16.0f) {
                        this.buttonSevereUnderWeight.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.buttonLessThan16.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.textViewResult.setTextColor(SupportMenu.CATEGORY_MASK);
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (bmi >= 16.0f && ((double) bmi) <= 18.5d) {
                        this.buttonRangeUnderWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.buttonUnderWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.textViewResult.setTextColor(Color.parseColor("#ffc04c"));
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (((double) bmi) > 18.5d && bmi <= 25.0f) {
                        this.buttonRangeNormal.setBackgroundColor(-16711936);
                        this.buttonNormal.setBackgroundColor(-16711936);
                        this.textViewResult.setTextColor(-16711936);
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (bmi > 25.0f && bmi <= 30.0f) {
                        this.buttonRangeOverWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.button1OverWeight.setBackgroundColor(Color.parseColor("#ffc04c"));
                        this.textViewResult.setTextColor(Color.parseColor("#ffc04c"));
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonObese.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                    if (bmi > 30.0f) {
                        this.buttonRangeObese.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.buttonObese.setBackgroundColor(SupportMenu.CATEGORY_MASK);
                        this.textViewResult.setTextColor(SupportMenu.CATEGORY_MASK);
                        this.buttonLessThan16.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonSevereUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonUnderWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonNormal.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.buttonRangeOverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                        this.button1OverWeight.setBackgroundDrawable(getResources().getDrawable(R.drawable.bmi_button_background));
                    }
                }
            }
        }
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        this.editTextHeight.setText("");
        this.editTextWeight.setText("");
        this.editTextHeightSecond.setText("");
        switch (id) {
            case R.id.radioButtonMetric:
                if (b) {
                    this.editTextHeight.setHint("cm");
                    this.editTextWeight.setHint("kg");
                    this.editTextHeightSecond.setVisibility(View.GONE);
                    return;
                }
                return;
            case R.id.radioButtonImperial:
                if (b) {
                    this.editTextHeight.setHint("feet");
                    this.editTextWeight.setHint("lb");
                    this.editTextHeightSecond.setHint("inch");
                    this.editTextHeightSecond.setVisibility(View.VISIBLE);
                    return;
                }
                return;
            default:
                return;
        }
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
                showInterstitialAds(AppConstants.ADMOB_BMI_INTERSTITIAL_AD_ID);
                this.interstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        BMIActivity.this.loadInterstitialAds(AppConstants.ADMOB_BMI_INTERSTITIAL_AD_ID);
                        BMIActivity.this.handleBMIBack();
                    }
                });
                return;
            }
            handleBMIBack();
            return;
        }
        super.onBackPressed();
    }
}
