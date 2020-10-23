package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.preview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.ads.listener.IAdCardClicked;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.custom.CustomPreviewActivity;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.exersices.ExerciseActivity;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabfour.type.ChallengeDaysType;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.home.tabs.tabfour.type.ExerciseType;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.iab.InAppActivity;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.listeners.EventCenter;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.Exercise;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.models.LevelData;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.ConsKeys;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SharedPrefsService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SimpleItemTouchHelperCallback;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;

public class LevelPreviewActivity extends InAppActivity implements IAdCardClicked {
    static final /* synthetic */ boolean $assertionsDisabled = (!LevelPreviewActivity.class.desiredAssertionStatus());
    @BindView(R.id.beginWorkout)
    AppCompatButton beginWorkout;
    @BindView(R.id.drag_drop)
    FrameLayout dragDropLayout;
    @BindView(R.id.drag_drop_text)
    TextView dragDropText;
    private LPreviewRA exAdapter;
    private List<Exercise> exerciseList;
    private boolean isRest = false;
    LevelData levelData;
    private AdView mAdView;
    private ChallengeDaysType mChallengeDaysType;
    private ExerciseType mExType;
    private FirebaseAnalytics mFirebaseAnalytics;
    @BindView(R.id.previewRV)
    RecyclerView previewRV;
    @BindView(R.id.previewToolbar)
    Toolbar previewToolbar;
    @BindView(R.id.previewToolbarAppbar)
    AppBarLayout previewToolbarAppbar;
    @BindView(R.id.previewToolbarCollapsing)
    CollapsingToolbarLayout previewToolbarCollapsing;
    @BindView(R.id.level_prev_redo)
    AppCompatImageView redoImageView;
    @BindView(R.id.restPreviewLayout)
    LinearLayout restPreviewLayout;
    private InterstitialAd mInterstitialAd;

    protected void onCreate(Bundle savedInstanceState) {
        int i = 0;
        super.onCreate(savedInstanceState);
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId("ca-app-pub-8468661407843417/6280037089");
        this.mInterstitialAd.loadAd(new Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        setContentView((int) R.layout.activity_level_preview);
        ButterKnife.bind((Activity) this);
        this.levelData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.EXERCISE_DATA_KEY);
        this.previewToolbar.setTitle((CharSequence) "");
        setSupportActionBar(this.previewToolbar);
        this.mExType = ExerciseType.getByType(getIntent().getExtras().getInt(ConsKeys.EXERCISE_DATA_TYPE_KEY));
        this.mAdView = (AdView) findViewById(R.id.adView);
        if (isProPackagePurchased()) {
            this.mAdView.setVisibility(View.GONE);
        } else {
            this.mAdView.loadAd(new Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        }
        if (this.levelData.getLevel().intValue() == -1) {
            this.previewToolbar.setTitle(getString(R.string.custom_workout));
            this.redoImageView.setVisibility(View.INVISIBLE);
        } else {
            int i2;
            this.previewToolbar.setTitle((this.mExType == ExerciseType.CHALLENGE ? getString(R.string.day) : getString(R.string.level)) + " " + this.levelData.getLevel());
            AppCompatImageView appCompatImageView = this.redoImageView;
            if (this.mExType == ExerciseType.CHALLENGE) {
                i2 = 4;
            } else {
                i2 = 0;
            }
            appCompatImageView.setVisibility(i2);
        }
        this.mChallengeDaysType = ChallengeDaysType.getByType(getIntent().getIntExtra(ConsKeys.EXERCISE_CHALLENGE_DAY_TYPE_KEY, 0));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFont();
        switch (this.mChallengeDaysType) {
            case BEGINNER:
            case MEDIUM:
            case PROFI:
                this.isRest = this.levelData.getLevel().intValue() % this.mChallengeDaysType.getRestInterval() == 0;
                break;
        }
        LinearLayout linearLayout = this.restPreviewLayout;
        if (!this.isRest) {
            i = 8;
        }
        linearLayout.setVisibility(i);
        if (this.isRest) {
            this.beginWorkout.setText(getString(R.string.rest));
        } else {
            setupAdapter(this.levelData.getExercises());
            showDragDropHelp(SharedPrefsService.getInstance().isLevelPreviewHintShowed(this));
            updateRemoveAdsUI();
        }
        if (!isProPackagePurchased()) {
            this.mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                }

                public void onAdClosed() {
                    LevelPreviewActivity.this.beginWorkoutClick();
                }
            });
        }
        this.beginWorkout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.e("beginWorkout","Click");
                if (isProPackagePurchased()) {
                    Log.e("beginWorkout","Click1");
                    LevelPreviewActivity.this.beginWorkoutClick();
                } else if (LevelPreviewActivity.this.mInterstitialAd.isLoaded()) {
                    Log.e("levan_interads","show");
                    LevelPreviewActivity.this.mInterstitialAd.show();
                } else {
                    Log.e("beginWorkout","Click2");
                    LevelPreviewActivity.this.beginWorkoutClick();
                }
            }
        });
        this.dragDropLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LevelPreviewActivity.this.dragDropLayout.setVisibility(8);
                SharedPrefsService.getInstance().setLevelPreviewHintShowed(LevelPreviewActivity.this, true);
            }
        });
        this.redoImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LevelPreviewActivity.this.deleteAddView();
                LevelData data = (LevelData) LevelPreviewActivity.this.getIntent().getExtras().getSerializable(ConsKeys.All_EXERCISE_KEY);
                Intent i = new Intent(LevelPreviewActivity.this, CustomPreviewActivity.class);
                i.putExtra(ConsKeys.LEVEL_PREV_DATA, LevelPreviewActivity.this.levelData);
                i.putExtra(ConsKeys.All_EXERCISE_KEY, data);
                LevelPreviewActivity.this.startActivity(i);
            }
        });
    }
    private void beginWorkoutClick() {
        if (LevelPreviewActivity.this.isRest) {
            LevelPreviewActivity.this.onBackPressed();
            return;
        }
        LevelPreviewActivity.this.deleteAddView();
        Intent intent = new Intent(LevelPreviewActivity.this, ExerciseActivity.class);
        intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, LevelPreviewActivity.this.levelData);
        intent.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, LevelPreviewActivity.this.mExType.getType());
        if (LevelPreviewActivity.this.levelData.getLevel().intValue() == -1) {
            SharedPrefsService.getInstance().setCustomWorkoutData(LevelPreviewActivity.this, LevelPreviewActivity.this.levelData.getExercises());
        }
        LevelPreviewActivity.this.startActivity(intent);
        Bundle params = new Bundle();
        params.putInt("level_" + LevelPreviewActivity.this.levelData.getLevel(), 1);
//                LevelPreviewActivity.this.mFirebaseAnalytics.logEvent("begin_workout", params);
        LevelPreviewActivity.this.finish();
    }

    private void deleteAddView() {
        List<Exercise> list = new ArrayList();
        for (Exercise ex : this.levelData.getExercises()) {
            if (ex.getViewType() != 6) {
                list.add(ex);
            }
        }
        this.levelData.setExercises(list);
    }

    private void showDragDropHelp(boolean show) {
        if (!show) {
            this.dragDropText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this));
            this.dragDropLayout.setVisibility(0);
        }
    }

    private void setupAdapter(List<Exercise> exerciseList) {
        this.previewRV.setLayoutManager(new GridLayoutManager(this,2));
        this.previewRV.setItemAnimator(new DefaultItemAnimator());
        this.exerciseList = exerciseList;
        this.exAdapter = new LPreviewRA(this, exerciseList, this);
        this.previewRV.setAdapter(this.exAdapter);
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this.exAdapter)).attachToRecyclerView(this.previewRV);
    }

    private void initFont() {
        setFont();
    }

    private void setFont() {
    }

    protected void updateRemoveAdsUI() {
        if (isProPackagePurchased()) {
            this.exAdapter.removeAds();
            EventCenter.getInstance().notifyUpdateRemoveAdsUI();
        }
    }

    protected void setWaitScreen(boolean set) {
    }

    public void removeAdClicked(int position) {
        onPurchaseClick();
    }

    public void whySeeAd() {
        whyAdDialog(this);
    }

    public void whyAdDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(1);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.unlock_dialog);
        dialog.setCancelable(true);
        dialog.show();
        Window window = dialog.getWindow();
        if ($assertionsDisabled || window != null) {
            window.setLayout(-1, -2);
            AppCompatButton unlockNow = (AppCompatButton) dialog.findViewById(R.id.dUnlockNow);
            AppCompatTextView descByRemoveAds = (AppCompatTextView) dialog.findViewById(R.id.descByRemoveAds);
            AppCompatTextView descYouHelpUs = (AppCompatTextView) dialog.findViewById(R.id.descYouHelpUs);
            AppCompatTextView descPresent = (AppCompatTextView) dialog.findViewById(R.id.descPresent);
            ((AppCompatTextView) dialog.findViewById(R.id.dialogTitle)).setTypeface(TypeFaceService.getInstance().getRobotoRegular(activity));
            descByRemoveAds.setTypeface(TypeFaceService.getInstance().getRobotoBold(activity));
            descYouHelpUs.setTypeface(TypeFaceService.getInstance().getRobotoBold(activity));
            unlockNow.setTypeface(TypeFaceService.getInstance().getRobotoRegular(activity));
            descPresent.setTypeface(TypeFaceService.getInstance().getRobotoRegular(activity));
            unlockNow.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    LevelPreviewActivity.this.removeAdClicked(0);
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }
            });
            return;
        }
        throw new AssertionError();
    }

    @Override
    public void onDestroy() {
        if(mAdView!=null){
            mAdView.destroy();
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ConsKeys.EXERCISE_DATA_IS_REST_KEY, this.isRest);
        intent.putExtra(ConsKeys.EXERCISE_DATA_CHALLENGE_DAY_KEY, this.levelData.getLevel());
        setResult(-1, intent);
        finish();
    }
}
