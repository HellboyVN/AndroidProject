package com.workout.workout.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalorieActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener {
    private Button ac11;
    private Button ac12;
    private Button back;
    private Button bc11;
    private Button bc12;
    private Button calc;
    LinearLayout calorietabletable;
    private Button cc11;
    private Button cc12;
    private Button dc11;
    private Button dc12;
    private Button ec11;
    private Button ec12;
    private EditText etc1;
    private EditText etc2;
    private EditText etc3;
    boolean isUserOnResultScreen = false;
    int itemPosition;
    boolean male = true;
    private RadioButton rbc1;
    private RadioButton rbc2;
    private RadioGroup rg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_body_calorie);
        Spinner spinner = (Spinner) findViewById(R.id.caloriesspinner);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList();
        categories.add("Basic Metabolism");
        categories.add("Sedentary: Little or no exercise");
        categories.add("Light: Exercise/sports 1-3 times a week");
        categories.add("Moderate: Exercise/sports 3-5 times a week");
        categories.add("Very Active: Hard exercise/sports 6-7 times a week");
        categories.add("Extra Active: Very hard exercise/sports/physical work");
        ArrayAdapter dataAdapter = new ArrayAdapter(this, R.layout.spinner_calorie, categories);
        dataAdapter.setDropDownViewResource(17367049);
        spinner.setAdapter(dataAdapter);
        this.calorietabletable = (LinearLayout) findViewById(R.id.calorieTable);
        this.rbc1 = (RadioButton) findViewById(R.id.rbc1);
        this.rbc2 = (RadioButton) findViewById(R.id.rbc2);
        this.rbc1.setOnCheckedChangeListener(this);
        this.rbc2.setOnCheckedChangeListener(this);
        if (FirebaseRemoteConfig.getInstance().getBoolean("bmi_calculator_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_CALORIE_BANNER_AD_ID);
        }
        loadInterstitialAds(AppConstants.ADMOB_CALORIE_INTERSTITIAL_AD_ID);
        initializeView();
    }

    public void onItemSelected(AdapterView parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        this.itemPosition = position;
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
        setToolbar("Calorie intake Calculator", true);
        this.ac11 = (Button) findViewById(R.id.ac11);
        this.ac12 = (Button) findViewById(R.id.ac12);
        this.bc11 = (Button) findViewById(R.id.bc11);
        this.bc12 = (Button) findViewById(R.id.bc12);
        this.cc11 = (Button) findViewById(R.id.cc11);
        this.cc12 = (Button) findViewById(R.id.cc12);
        this.dc11 = (Button) findViewById(R.id.dc11);
        this.dc12 = (Button) findViewById(R.id.dc12);
        this.ec11 = (Button) findViewById(R.id.ec11);
        this.ec12 = (Button) findViewById(R.id.ec12);
        this.calc = (Button) findViewById(R.id.buttonCalculateCaloriesIntake);
        this.etc1 = (EditText) findViewById(R.id.etc1);
        this.etc2 = (EditText) findViewById(R.id.etc2);
        this.etc3 = (EditText) findViewById(R.id.etc3);
        this.rbc1 = (RadioButton) findViewById(R.id.rbc1);
        this.rbc2 = (RadioButton) findViewById(R.id.rbc2);
        this.calc.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCalculateCaloriesIntake:
                calculateAndSetViews();
                return;
            default:
                return;
        }
    }

    private void calculateAndSetViews() {
        String s1 = this.etc1.getText().toString();
        String s2 = this.etc2.getText().toString();
        String s3 = this.etc3.getText().toString();
        if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        double age = (double) Integer.parseInt(s1);
        double weight = (double) Integer.parseInt(s2);
        double height = (double) Integer.parseInt(s3);
        if (age == 0.0d || weight == 0.0d || height == 0.0d) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }
        double calorie;
        if (this.male) {
            calorie = (((10.0d * weight) + (6.25d * height)) - (5.0d * age)) + 5.0d;
        } else {
            calorie = (((10.0d * weight) + (6.25d * height)) - (5.0d * age)) - 161.0d;
        }
        if (calorie <= 1.0d || calorie >= 20000.0d) {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show();
            return;
        }
        this.calorietabletable.setVisibility(View.VISIBLE);
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        if (this.itemPosition != 0) {
            if (this.itemPosition == 1) {
                calorie *= 1.2d;
            } else if (this.itemPosition == 2) {
                calorie *= 1.375d;
            } else if (this.itemPosition == 3) {
                calorie *= 1.55d;
            } else if (this.itemPosition == 4) {
                calorie *= 1.725d;
            } else {
                calorie *= 1.9d;
            }
        }
        try {
            Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Calorie Calculated").putCustomAttribute("calorie value", Double.valueOf(calorie)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.ac12.setText(new DecimalFormat("0.00").format(calorie));
        this.bc12.setText(new DecimalFormat("0.00").format(calorie / 1.391d));
        this.cc12.setText(new DecimalFormat("0.00").format(calorie / 2.28865d));
        this.dc12.setText(new DecimalFormat("0.00").format(1.281d * calorie));
        this.ec12.setText(new DecimalFormat("0.00").format(1.56306d * calorie));
    }

    public void onBackPressed() {
        boolean showInterstitialAd;
        if (((long) PersistenceManager.getBMICalculateCount()) % ((long) PersistenceManager.getAdmob_bmi_interstitial_ads_interval()) == 0) {
            showInterstitialAd = true;
        } else {
            showInterstitialAd = false;
        }
        if (this.interstitialAd != null && this.interstitialAd.isLoaded() && showInterstitialAd) {
            showInterstitialAds(AppConstants.ADMOB_CALORIE_INTERSTITIAL_AD_ID);
            this.interstitialAd.setAdListener(new AdListener() {
                public void onAdClosed() {
                    super.onAdClosed();
                    CalorieActivity.this.loadInterstitialAds(AppConstants.ADMOB_CALORIE_INTERSTITIAL_AD_ID);
                }
            });
        }
        super.onBackPressed();
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.rbc1:
                if (b) {
                    this.male = true;
                    return;
                }
                return;
            case R.id.rbc2:
                if (b) {
                    this.male = false;
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
