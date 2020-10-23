package hb.me.homeworkout.gym.buttlegs.buttlegspro.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.InAppActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.LevelPreviewActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SnackBarService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ThemeUtils;

public class CustomPreviewActivity extends InAppActivity implements IDurationChange, IPreviousWorkoutAactions {
    public static final int EXERCISE_MAX_COUNT = 12;
    public static final int EXERCISE_MIN_COUNT = 2;
    static final String TAG = "CustomPreviewActivity";
    LCustomRA adapter;
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
    ExerciseType mExType;
    List<Exercise> mExercises;
    LevelData ollLevelData;
    @BindView(R.id.totalTime)
    AppCompatTextView totalTime;
    @BindView(R.id.adView)
    AdView adView;

    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.CustomPreviewActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.CustomPreviewActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.CustomPreviewActivity");
        super.onCreate(bundle);
        ThemeUtils.onActivityCreateSetTheme(this);
        setContentView((int) R.layout.activity_custom_preview);
        ButterKnife.bind((Activity) this);
        this.ollLevelData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.Oll_EXERCISE_KEY);
        this.mExType = ExerciseType.getByType(getIntent().getExtras().getInt(ConsKeys.EXERCISE_DATA_TYPE_KEY));
        changeOllLevelData();
        setSupportActionBar(this.customToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        this.customToolbar.setTitle(getString(R.string.custom_workout));
        initFont();
        this.mExercises = this.ollLevelData.getExercises();
        onChange(0);
        setupAdapter(this.mExercises);
        this.ollLevelData.setLevel(Integer.valueOf(-1));
        this.beginCustomWorkout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                List<Exercise> ex = CustomPreviewActivity.this.filterExercise();
                LevelData data = new LevelData();
                data.setExercises(ex);
                data.setLevel(Integer.valueOf(-1));
                data.setNameKey("custom_workout");
                CustomPreviewActivity.this.onBeginButtonClicked(data);
            }
        });
        admobBanner(adView);
//        adView = new AdView(this)
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId(ConsKeys.BANNER_BEGIN_WORKOUT);
//        adView.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
       // updateRemoveAdsUI();
    }

    @Override
    public void onDestroy() {
        if (adView !=null){
            adView.destroy();
        }
        super.onDestroy();
        Log.e("levan------","finish");
        finish();
    }

    private int loadSavedData(List<Exercise> exercises) {
        int dur = 0;
        List<Exercise> savedExercises = SharedPrefsService.getInstance().getCustomWorkoutData(this, this.mExType);
        for (Exercise ex : exercises) {
            ex.setDuration(Integer.valueOf(0));
            for (Exercise savedEx : savedExercises) {
                if (ex.getNameKey() != null && ex.getNameKey().equals(savedEx.getNameKey())) {
                    ex.setDuration(savedEx.getDuration());
                    dur += savedEx.getDuration().intValue();
                }
            }
        }
        return dur;
    }

    public List<Exercise> filterExercise() {
        List<Exercise> exList = new ArrayList();
        for (Exercise ex : this.ollLevelData.getExercises()) {
            if (!(ex.getNameKey() == null || ex.getDuration().equals(Integer.valueOf(0)))) {
                exList.add(ex);
            }
        }
        return exList;
    }

    private void setupAdapter(List<Exercise> exerciseList) {
        if (SharedPrefsService.getInstance().getCustomWorkoutData(this, this.mExType).size() > 0) {
            exerciseList.add(0, new Exercise());
        }
        this.customRV.setLayoutManager(new LinearLayoutManager(this));
        this.customRV.setItemAnimator(new DefaultItemAnimator());
        this.adapter = new LCustomRA(this, exerciseList, this.mExType, this);
        this.customRV.setAdapter(this.adapter);
    }

    private void initFont() {
        setFont();
    }

    private void setFont() {
    }

    private void changeOllLevelData() {
        this.levelPrevData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.LEVEL_PREV_DATA);
        if (this.levelPrevData != null) {
            for (int i = 0; i < this.ollLevelData.getExercises().size(); i++) {
                for (Exercise exercise : this.levelPrevData.getExercises()) {
                    if (((Exercise) this.ollLevelData.getExercises().get(i)).getNameKey().equals(exercise.getNameKey())) {
                        this.ollLevelData.getExercises().set(i, exercise);
                    }
                }
                this.duration = ((Exercise) this.ollLevelData.getExercises().get(i)).getDuration().intValue() + this.duration;
            }
        }
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
        if (data.getExercises().size() >= 2 && data.getExercises().size() <= 12) {
            Intent intent = new Intent(this, LevelPreviewActivity.class);
            intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, data);
            intent.putExtra(ConsKeys.Oll_EXERCISE_KEY, this.ollLevelData);
            intent.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, this.mExType.getType());
            startActivityForResult(intent, ConsKeys.REQUEST_LEVEL_PREVIEW);
        } else if (data.getExercises().size() < 2) {
            SnackBarService.showQuickSnackBar(this, getString(R.string.custom_workout_min, new Object[]{Integer.valueOf(2)}));
        } else if (data.getExercises().size() > 12) {
            SnackBarService.showQuickSnackBar(this, getString(R.string.custom_workout_max, new Object[]{Integer.valueOf(12)}));
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConsKeys.REQUEST_LEVEL_PREVIEW) {
            if (((Exercise) this.mExercises.get(0)).getNameKey() != null && SharedPrefsService.getInstance().getCustomWorkoutData(this, this.mExType).size() > 0) {
                this.mExercises.add(0, new Exercise());
            }
            this.adapter.notifyDataSetChanged();
        }
    }

    protected void updateRemoveAdsUI() {
        if (isRemoveAdsPurchased()) {
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

    protected void iabSetupFailed() {
    }

    public void onPreview(List<Exercise> exercises) {
        LevelData data = new LevelData();
        data.setExercises(exercises);
        data.setLevel(Integer.valueOf(-1));
        data.setNameKey("custom_workout");
        onBeginButtonClicked(data);
    }

    public void onRestore() {
        this.duration = 0;
        onChange(loadSavedData(this.mExercises));
        this.adapter.notifyDataSetChanged();
        SnackBarService.showQuickSnackBar(this, getString(R.string.restored));
    }
}
