package hb.me.homeworkout.gym.buttlegs.buttlegspro.preview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
//import com.google.firebase.perf.metrics.AppStartTrace;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.ads.listener.IAdCardClicked;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.custom.CustomPreviewActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ChallengeDaysType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.iab.InAppActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RecyclerItemClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RecyclerItemClickListener.OnItemClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SimpleItemTouchHelperCallback;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;
import java.util.ArrayList;
import java.util.List;

public class LevelPreviewActivity extends InAppActivity implements IAdCardClicked {
    private String FIREBASE_LEVEL = "level_";
    @BindView(R.id.beginWorkout)
    AppCompatButton beginWorkout;
    @BindView(R.id.drag_drop)
    FrameLayout dragDropLayout;
    @BindView(R.id.drag_drop_text)
    TextView dragDropText;
    private LevelPreviewRA exAdapter;
    private List<Exercise> exerciseList;
    private boolean isRest = false;
    LevelData levelData;
    private ChallengeDaysType mChallengeDaysType;
    private ExerciseType mExType;
    private InterstitialAd mInterstitialAd;
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
    @BindView(R.id.adView)
    AdView adView1;
    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.LevelPreviewActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.LevelPreviewActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
        int i = 0;
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.preview.LevelPreviewActivity");
        super.onCreate(bundle);
        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(ConsKeys.INTERSTITIAL_BEGIN_WORKOUT);
        this.mInterstitialAd.loadAd(new Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
        setContentView((int) R.layout.activity_level_preview);
        ButterKnife.bind((Activity) this);
        this.levelData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.EXERCISE_DATA_KEY);
        this.previewToolbar.setTitle((CharSequence) "");
        setSupportActionBar(this.previewToolbar);
        this.mExType = ExerciseType.getByType(getIntent().getExtras().getInt(ConsKeys.EXERCISE_DATA_TYPE_KEY));
        if (this.levelData.getLevel().intValue() == -1) {
            this.previewToolbar.setTitle(getString(R.string.custom_workout));
            this.redoImageView.setVisibility(4);
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
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
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
        if (!isRemoveAdsPurchased()) {
            this.mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                }

                public void onAdClosed() {
                    LevelPreviewActivity.this.beginWorkoutClick();
                }
            });
        }
        if (this.isRest) {
            this.beginWorkout.setText(getString(R.string.rest));
        } else {
            setupAdapter(this.levelData.getExercises());
            showDragDropHelp(SharedPrefsService.getInstance().isLevelPreviewHintShowed(this));
            updateRemoveAdsUI();
        }
        this.beginWorkout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Log.e("beginWorkout","Click");
                if (LevelPreviewActivity.this.isRemoveAdsPurchased()) {
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
        this.redoImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LevelPreviewActivity.this.deletAddView();
                LevelData data = (LevelData) LevelPreviewActivity.this.getIntent().getExtras().getSerializable(ConsKeys.Oll_EXERCISE_KEY);
                Intent i = new Intent(LevelPreviewActivity.this, CustomPreviewActivity.class);
                i.putExtra(ConsKeys.LEVEL_PREV_DATA, LevelPreviewActivity.this.levelData);
                i.putExtra(ConsKeys.Oll_EXERCISE_KEY, data);
                i.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, ExerciseType.TRAIN_AT_HOME.getType());
                LevelPreviewActivity.this.startActivity(i);
            }
        });
        this.dragDropLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LevelPreviewActivity.this.dragDropLayout.setVisibility(8);
                SharedPrefsService.getInstance().setLevelPreviewHintShowed(LevelPreviewActivity.this, true);
            }
        });
        admobBanner(adView1);
        admobBanner(adView1);
    }

    private void beginWorkoutClick() {
        if (this.isRest) {
            onBackPressed();
            return;
        }
        deletAddView();
        if (this.levelData.getLevel().intValue() == -1) {
            SharedPrefsService.getInstance().setCustomWorkoutData(this, this.levelData.getExercises(), this.mExType);
        }
        Bundle params = new Bundle();
        params.putInt(this.FIREBASE_LEVEL + this.levelData.getLevel(), 1);
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, this.levelData);
        intent.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, this.mExType.getType());
        startActivity(intent);
        SharedPrefsService.getInstance().setLevelPreviewHintShowed(this, true);
        finish();
    }

    private void deletAddView() {
        List<Exercise> list = new ArrayList();
        for (Exercise ex : this.levelData.getExercises()) {
            if (ex.getViewType() != 4) {
                list.add(ex);
            }
        }
        this.levelData.setExercises(list);
    }

    private void showDragDropHelp(boolean show) {
        this.dragDropLayout.setVisibility(show ? 8 : 0);
    }

    private void setupAdapter(final List<Exercise> exerciseList) {
        this.previewRV.setLayoutManager(new LinearLayoutManager(this));
        this.previewRV.setItemAnimator(new DefaultItemAnimator());
        this.exerciseList = exerciseList;
//        if (!isRemoveAdsPurchased()) {
//            for (int i = 0; i <= exerciseList.size(); i += 5) {
//                NativeExpressAdView adView = new NativeExpressAdView(this);
//                Exercise adCard = new Exercise();
//                adCard.setViewType(4);
//                adCard.setAdView(adView);
//                exerciseList.add(i, adCard);
//            }
//            //setUpAndLoadNativeExpressAds(0, this.previewRV, this.exerciseList);
//        }
        this.exAdapter = new LevelPreviewRA(this, exerciseList, this);
        this.previewRV.setAdapter(this.exAdapter);
        new ItemTouchHelper(new SimpleItemTouchHelperCallback(this.exAdapter)).attachToRecyclerView(this.previewRV);
        this.previewRV.addOnItemTouchListener(new RecyclerItemClickListener(this, this.previewRV, new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                switch (((Exercise) exerciseList.get(position)).getViewType()) {
                }
            }

            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    private void initFont() {
        setFont();
    }

    private void setFont() {
        this.dragDropText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this));
    }

    public void removeAdClicked(int position) {
        onPurchaseClick();
    }

    protected void updateRemoveAdsUI() {
        if (isRemoveAdsPurchased() && this.exAdapter != null) {
            this.exAdapter.removeAds();
            EventCenter.getInstance().notifyUpdateRemoveAdsUI();
        }
    }

    protected void setWaitScreen(boolean set) {
    }

    protected void iabSetupFailed() {
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ConsKeys.EXERCISE_DATA_IS_REST_KEY, this.isRest);
        intent.putExtra(ConsKeys.EXERCISE_DATA_CHALLENGE_DAY_KEY, this.levelData.getLevel());
        setResult(-1, intent);
        finish();
    }

    @Override
    public void onDestroy() {
        if(adView1!=null){
            adView1.destroy();
        }
        super.onDestroy();
    }
}
