package hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.R;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.iab.InAppActivity;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.listeners.EventCenter;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.Exercise;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.models.LevelData;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.preview.LevelPreviewActivity;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.ConsKeys;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SharedPrefsService;
import hb.legsandbuttocksworkout.homeworkout.gym.buttlegs.buttlegspro.buttlegschallenge.utils.SnackBarService;

public class CustomPreviewActivity extends InAppActivity implements IDurationChange, IPreviousWorkoutAactions {
    static final String TAG = "CustomPreviewActivity";
    LCustomRA adapter;
    LevelData allLevelData;
    @BindView(R.id.beginCustomWorkout)
    LinearLayout beginCustomWorkout;
    @BindView(R.id.beginCustomWorkoutButton)
    AppCompatTextView beginCustomWorkoutButton;
    @BindView(R.id.customRV)
    RecyclerView customRV;
    @BindView(R.id.customToolbar)
    Toolbar customToolbar;
    @BindView(R.id.customToolbarAppbar)
    AppBarLayout customToolbarAppbar;
    @BindView(R.id.customToolbarCollapsing)
    CollapsingToolbarLayout customToolbarCollapsing;
    @BindView(R.id.customWaitScreen)
    FrameLayout customWaitScreen;
    private int duration = 0;
    LevelData levelPrevData;
    List<Exercise> mExercises;
    @BindView(R.id.totalTime)
    AppCompatTextView totalTime;
    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_custom_preview);
        ButterKnife.bind((Activity) this);
        this.allLevelData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.All_EXERCISE_KEY);
        this.levelPrevData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.LEVEL_PREV_DATA);
        if (this.levelPrevData != null) {
            changeAllLevelData();
        }
        setSupportActionBar(this.customToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        this.mAdView = (AdView) findViewById(R.id.adView);
        if (isProPackagePurchased()) {
            this.mAdView.setVisibility(View.GONE);
        } else {
            this.mAdView.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        }
        this.customToolbar.setTitle(getString(R.string.custom_workout));
        initFont();
        this.mExercises = this.allLevelData.getExercises();
        onChange(0);
        setupAdapter(this.mExercises);
        this.allLevelData.setLevel(Integer.valueOf(-1));
        this.beginCustomWorkout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                List<Exercise> ex = CustomPreviewActivity.this.filterExercise();
                LevelData data = new LevelData();
                data.setExercises(ex);
                data.setLevel(Integer.valueOf(-1));
                data.setNameKey(ConsKeys.PREFS_IAP_CUSTOM_WORKOUT);
                CustomPreviewActivity.this.onBeginButtonClicked(data);
            }
        });
        updateRemoveAdsUI();
    }

    private void changeAllLevelData() {
        if (this.levelPrevData != null) {
            for (int i = 0; i < this.allLevelData.getExercises().size(); i++) {
                for (Exercise exercise : this.levelPrevData.getExercises()) {
                    if (((Exercise) this.allLevelData.getExercises().get(i)).getNameKey().equals(exercise.getNameKey())) {
                        this.allLevelData.getExercises().set(i, exercise);
                    }
                }
                this.duration = ((Exercise) this.allLevelData.getExercises().get(i)).getDuration().intValue() + this.duration;
            }
        }
    }

    private void loadSavedData(List<Exercise> exercises) {
        List<Exercise> savedExercises = SharedPrefsService.getInstance().getCustomWorkoutData(this);
        for (Exercise ex : exercises) {
            ex.setDuration(Integer.valueOf(0));
            for (Exercise savedEx : savedExercises) {
                if (ex.getNameKey() != null && ex.getNameKey().equals(savedEx.getNameKey())) {
                    ex.setDuration(savedEx.getDuration());
                    this.duration += savedEx.getDuration().intValue();
                }
            }
        }
    }

    public List<Exercise> filterExercise() {
        List<Exercise> exList = new ArrayList();
        for (Exercise ex : this.allLevelData.getExercises()) {
            if (!(ex.getNameKey() == null || ex.getDuration().equals(Integer.valueOf(0)))) {
                exList.add(ex);
            }
        }
        return exList;
    }

    private void setupAdapter(List<Exercise> exerciseList) {
        if (isProPackagePurchased() && SharedPrefsService.getInstance().getCustomWorkoutData(this).size() > 0) {
            exerciseList.add(0, new Exercise());
        }
        this.customRV.setLayoutManager(new GridLayoutManager(this,2));
        this.customRV.setItemAnimator(new DefaultItemAnimator());
        this.adapter = new LCustomRA(this, exerciseList, this);
        this.customRV.setAdapter(this.adapter);
    }

    private void initFont() {
        setFont();
    }

    private void setFont() {
    }

    public void onChange(int duration) {
        this.duration += duration;
        constructTime();
    }

    private void constructTime() {
        int hour = (this.duration / 1000) / 60;
        int minute = (this.duration / 1000) % 60;
        this.totalTime.setText((hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute)) + getResources().getString(R.string.minute_short));
    }

    public void onBeginButtonClicked(LevelData data) {
//        if (!isProPackagePurchased()) {
//            onPurchaseClick();
//        } else
            if (data.getExercises().size() >= 2 && data.getExercises().size() <= 7) {
            Intent intent = new Intent(this, LevelPreviewActivity.class);
            intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, data);
            intent.putExtra(ConsKeys.All_EXERCISE_KEY, this.allLevelData);
            startActivityForResult(intent, REQUEST_LEVEL_PREVIEW);
        } else if (data.getExercises().size() < 2) {
            SnackBarService.showQuickSnackBar(this, getString(R.string.min_2));
        } else if (data.getExercises().size() > 7) {
            SnackBarService.showQuickSnackBar(this, getString(R.string.max_7));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LEVEL_PREVIEW) {
            if (((Exercise) this.mExercises.get(0)).getNameKey() != null && SharedPrefsService.getInstance().getCustomWorkoutData(this).size() > 0) {
                this.mExercises.add(0, new Exercise());
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void updateRemoveAdsUI() {
        this.beginCustomWorkoutButton.setCompoundDrawablesWithIntrinsicBounds(isProPackagePurchased() ? 0 : 0 /*R.drawable.ic_lock*/, 0, 0, 0);
        if (isProPackagePurchased()) {
            EventCenter.getInstance().notifyUpdateRemoveAdsUI();
        }
    }

    protected void setWaitScreen(boolean set) {
        boolean z;
        boolean z2 = true;
        this.customWaitScreen.setVisibility(set ? 0 : 8);
        LinearLayout linearLayout = this.beginCustomWorkout;
        if (set) {
            z = false;
        } else {
            z = true;
        }
        linearLayout.setClickable(z);
        LinearLayout linearLayout2 = this.beginCustomWorkout;
        if (set) {
            z2 = false;
        }
        linearLayout2.setEnabled(z2);
    }

    public void onPreview(List<Exercise> exercises) {
        LevelData data = new LevelData();
        data.setExercises(exercises);
        data.setLevel(Integer.valueOf(-1));
        data.setNameKey(ConsKeys.PREFS_IAP_CUSTOM_WORKOUT);
        onBeginButtonClicked(data);
    }

    public void onRestore() {
        loadSavedData(this.mExercises);
        onChange(this.duration);
        this.adapter.notifyDataSetChanged();
        SnackBarService.showQuickSnackBar(this, getString(R.string.restored));
    }
    @Override
    public void onDestroy() {
        if(mAdView!=null){
            mAdView.destroy();
        }
        super.onDestroy();
    }
}
