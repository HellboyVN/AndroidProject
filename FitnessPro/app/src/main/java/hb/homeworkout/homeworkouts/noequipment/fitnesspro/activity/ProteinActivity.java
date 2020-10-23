package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;

public class ProteinActivity extends BaseActivity {
    private Button buttonBack;
    private Button buttonCalculate;
    private EditText editTextWeight;
    private boolean isUserOnResultScreen;
    private LinearLayout linearLayoutFitnessLevel;
    private LinearLayout linearLayoutWeight;
    private RadioButton radioButtonActiveLevelTraining;
    private RadioButton radioButtonFemale;
    private RadioButton radioButtonLowLevelTraining;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonNoExercise;
    private RadioButton radioButtonSports;
    private RadioButton radioButtonWeightTraining;
    private RadioGroup radioGroupFitnessLevel;
    private RadioGroup radioGroupGender;
    private TextView textViewFitnessLevel;
    private TextView textViewGender;
    private TextView textViewProtein;
    private float weight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_protein);
        loadBannerAdvertisement(this);
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
        setToolbar("Protein Body Index", true);
        this.editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        this.radioGroupGender = (RadioGroup) findViewById(R.id.radioGroupGender);
        this.radioGroupFitnessLevel = (RadioGroup) findViewById(R.id.radioGroupFitnessLevel);
        this.textViewProtein = (TextView) findViewById(R.id.textViewProtein);
        this.radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);
        this.radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        this.radioButtonNoExercise = (RadioButton) findViewById(R.id.radioButtonNoExercise);
        this.radioButtonLowLevelTraining = (RadioButton) findViewById(R.id.radioButtonLowLevelTraining);
        this.radioButtonActiveLevelTraining = (RadioButton) findViewById(R.id.radioButtonActiveLevelTraining);
        this.radioButtonSports = (RadioButton) findViewById(R.id.radioButtonSports);
        this.radioButtonWeightTraining = (RadioButton) findViewById(R.id.radioButtonWeightTraining);
        this.textViewFitnessLevel = (TextView) findViewById(R.id.textViewFitnessLevel);
        this.textViewGender = (TextView) findViewById(R.id.textViewGender);
        this.buttonCalculate = (Button) findViewById(R.id.buttonCalculate);
        this.buttonBack = (Button) findViewById(R.id.buttonBack);
        this.linearLayoutFitnessLevel = (LinearLayout) findViewById(R.id.linearLayoutFitnessLevel);
        this.linearLayoutWeight = (LinearLayout) findViewById(R.id.linearLayoutWeight);
        this.buttonCalculate.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ProteinActivity.this.handleCalculateProtein();
            }
        });
        this.buttonBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                showInterstitialAds();
                boolean showInterstitialAd;
                if (((long) PersistenceManager.getProteinCalculateCount()) % ((long) PersistenceManager.getAdmob_protein_interstitial_ads_interval()) == 0) {
                    showInterstitialAd = true;
                } else {
                    showInterstitialAd = false;
                }
                if (ProteinActivity.this.interstitialAd != null && ProteinActivity.this.interstitialAd.isLoaded() && showInterstitialAd) {
                    ProteinActivity.this.interstitialAd.setAdListener(new AdListener() {
                        public void onAdClosed() {
                            super.onAdClosed();
                            ProteinActivity.this.handleBackButton();
                        }
                    });
                    return;
                }
                ProteinActivity.this.handleBackButton();
            }
        });
    }

    private void handleBackButton() {
        this.buttonCalculate.setVisibility(View.VISIBLE);
        this.linearLayoutFitnessLevel.setVisibility(View.VISIBLE);
        this.textViewFitnessLevel.setVisibility(View.VISIBLE);
        this.linearLayoutWeight.setVisibility(View.VISIBLE);
        this.radioGroupGender.setVisibility(View.VISIBLE);
        this.textViewGender.setVisibility(View.VISIBLE);
        this.textViewProtein.setVisibility(View.GONE);
        this.buttonBack.setVisibility(View.GONE);
        this.isUserOnResultScreen = false;
    }

    private void handleCalculateProtein() {
        String weightText = this.editTextWeight.getText().toString();
        if (TextUtils.isEmpty(weightText)) {
            Toast.makeText(this, "Please enter weight first", Toast.LENGTH_SHORT).show();
            this.isUserOnResultScreen = false;
            return;
        }
        this.weight = Float.valueOf(weightText).floatValue();
        double protein1;
        if (this.radioButtonMale.isChecked()) {
            if (this.radioButtonNoExercise.isChecked()) {
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 0.75d) + "  grams"));
            } else if (this.radioButtonLowLevelTraining.isChecked()) {
                protein1 = ((double) this.weight) * 0.9d;
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 0.8d) + " to " + protein1 + "  grams"));
            } else if (this.radioButtonActiveLevelTraining.isChecked()) {
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 1.2d) + "  grams"));
            } else if (this.radioButtonSports.isChecked()) {
                protein1 = ((double) this.weight) * 1.7d;
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 1.3d) + " to " + protein1 + "  grams"));
            } else if (this.radioButtonWeightTraining.isChecked()) {
                protein1 = ((double) this.weight) * 1.8d;
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 1.5d) + " to " + protein1 + "  grams"));
            }
        } else if (this.radioButtonFemale.isChecked()) {
            if (this.radioButtonNoExercise.isChecked()) {
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 0.75d) + "  grams"));
            } else if (this.radioButtonLowLevelTraining.isChecked()) {
                protein1 = ((double) this.weight) * 0.9d;
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 0.8d) + " to " + protein1 + "  grams"));
            } else if (this.radioButtonActiveLevelTraining.isChecked()) {
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 1.2d) + "  grams"));
            } else if (this.radioButtonSports.isChecked()) {
                protein1 = ((double) this.weight) * 1.4d;
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 1.2d) + " to " + protein1 + "  grams"));
            } else if (this.radioButtonWeightTraining.isChecked()) {
                protein1 = ((double) this.weight) * 1.4d;
                this.textViewProtein.setText(String.valueOf("Required Protein is " + (((double) this.weight) * 1.3d) + " to " + protein1 + "  grams"));
            }
        }
        this.buttonCalculate.setVisibility(View.GONE);
        this.linearLayoutFitnessLevel.setVisibility(View.GONE);
        this.textViewFitnessLevel.setVisibility(View.GONE);
        this.linearLayoutWeight.setVisibility(View.GONE);
        this.radioGroupGender.setVisibility(View.GONE);
        this.textViewGender.setVisibility(View.GONE);
        this.textViewProtein.setVisibility(View.VISIBLE);
        this.buttonBack.setVisibility(View.VISIBLE);
        this.isUserOnResultScreen = true;
//        try {
//            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Protein Calculated"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void onBackPressed() {
        if (this.isUserOnResultScreen) {
            boolean showInterstitialAd;
            if (((long) PersistenceManager.getProteinCalculateCount()) % ((long) PersistenceManager.getAdmob_protein_interstitial_ads_interval()) == 0) {
                showInterstitialAd = true;
            } else {
                showInterstitialAd = false;
            }
            if (this.interstitialAd != null && this.interstitialAd.isLoaded() && showInterstitialAd) {
                this.interstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        ProteinActivity.this.handleBackButton();
                    }
                });
                return;
            }
            handleBackButton();
            return;
        }
        super.onBackPressed();
    }
}
