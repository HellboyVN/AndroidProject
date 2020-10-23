package com.workout.workout.activity;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.os.EnvironmentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.Workout;
import com.workout.workout.util.AppUtil;
import com.workout.workout.util.CountDownTimerWithPause;

import org.apache.commons.cli.HelpFormatter;

import java.util.ArrayList;
import java.util.Iterator;

import io.fabric.sdk.android.services.settings.SettingsJsonConstants;

public class WorkoutTrackerStart extends BaseActivity implements OnClickListener {
    private CardView cardViewGetReady;
    private CardView cardViewVideo;
    private CountDownTimerWithPause countDownTimer;
    private Plan currentPlan;
    private int currentPosition = 0;
    private int currentSet;
    private String currentSetsAndRepsString;
    private Workout currentWorkout;
    private String currentWorkoutImageUrl;
    private String currentWorkoutName;
    private DonutProgress donutProgressTimer;
    private int dynamicRepTime = 0;
    private int[] exerciseTimeArray;
    private FloatingActionButton floatingButtonPlayPause;
    private int getReadyTimer = 20;
    private ImageView imageViewWorkoutExercise;
    private boolean isGetReadyState = true;
    private boolean isMyPlan;
    private boolean isPausedState;
    private boolean isRestingState = true;
    private boolean isThreeTwoOneGoSoundPausedState = false;
    private boolean isTickSoundPausedState = false;
    private String load;
    private MediaPlayer mediaPlayerThreeTwoOneGoSound;
    private MediaPlayer mediaplayerTickSound;
    private Plan plan;
    private ArrayList<Plan> planWorkoutList = new ArrayList();
    private ProgressBar progressBarVideo;
    private ProgressBar progressBarWorkoutExerciseImage;
    private RelativeLayout relativeLayoutWorkoutExercise;
    private int[] repsArray;
    private int rest;
    private int sets;
    private String speed;
    private long startTime = System.currentTimeMillis();
    private TextView textViewExerciseName;
    private TextView textViewExerciseNumber;
    private TextView textViewFirstExercise;
    private TextView textViewGetReady;
    private TextView textViewLoad;
    private TextView textViewRep;
    private TextView textViewSet;
    private TextView textViewSpeed;
    private TextView textViewWorkoutExerciseName;
    private TextView textViewWorkoutExerciseSetsAndReps;
    private int totalExercises;
    private ValueAnimator valueAnimator;
    private VideoView videoView;
    private ArrayList<Workout> workoutList = new ArrayList();

    private class MyCountDownTimer extends CountDownTimerWithPause {
        private MyCountDownTimer(long millisInFuture, long countDownInterval, boolean boo) {
            super(millisInFuture, countDownInterval, boo);
        }

        public void onTick(long millisUntilFinished) {
            int progress = (int) (millisUntilFinished / 1000);
            if (WorkoutTrackerStart.this.isGetReadyState) {
                WorkoutTrackerStart.this.donutProgressTimer.setProgress((float) (WorkoutTrackerStart.this.getReadyTimer - progress));
                Log.w("donutProgressTimer", "donutProgressTimer setting isGetReadyState progress = " + (WorkoutTrackerStart.this.getReadyTimer - progress));
            } else if (WorkoutTrackerStart.this.isRestingState) {
                WorkoutTrackerStart.this.donutProgressTimer.setProgress((float) (WorkoutTrackerStart.this.rest - progress));
                Log.w("donutProgressTimer", "donutProgressTimer setting isRestingState progress = (rest = " + WorkoutTrackerStart.this.rest + "- progress = " + progress + ") = " + (WorkoutTrackerStart.this.rest - progress));
            } else {
                WorkoutTrackerStart.this.donutProgressTimer.setProgress((float) (WorkoutTrackerStart.this.dynamicRepTime - progress));
                Log.w("donutProgressTimer", "donutProgressTimer setting dynamicRepTime progress = " + (WorkoutTrackerStart.this.dynamicRepTime - progress));
            }
            WorkoutTrackerStart.this.donutProgressTimer.setText(String.valueOf(progress) + "s");
            Log.w("donutProgressTimer", "donutProgressTimer setting text = " + String.valueOf(progress) + "s");
            if (WorkoutTrackerStart.this.mediaplayerTickSound != null) {
                WorkoutTrackerStart.this.mediaplayerTickSound.start();
            }
            if (progress == 3) {
                if (WorkoutTrackerStart.this.isThreeTwoOneGoSoundPausedState) {
                    WorkoutTrackerStart.this.mediaPlayerThreeTwoOneGoSound.seekTo(0);
                    WorkoutTrackerStart.this.isThreeTwoOneGoSoundPausedState = false;
                }
                WorkoutTrackerStart.this.mediaPlayerThreeTwoOneGoSound.start();
            }
        }

        public void onFinish() {
            WorkoutTrackerStart.this.isGetReadyState = false;
            WorkoutTrackerStart.this.isRestingState = !WorkoutTrackerStart.this.isRestingState;
            WorkoutTrackerStart.this.donutProgressTimer.setText(String.valueOf(0));
            Log.w("donutProgressTimer", "donutProgressTimer setting text = " + String.valueOf(0) + "s");
            WorkoutTrackerStart.this.updateTrackerToNextSetOrExerciseOrEndTheTracker();
        }
    }

    private class PlanWorkoutListAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlanWorkoutListAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            WorkoutTrackerStart.this.preparePlanWorkoutList();
            WorkoutTrackerStart.this.prepareWorkoutVideosList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            WorkoutTrackerStart.this.initializeEverything();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.layout_workout_start);
        try {
            Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Tracker started for plan").putCustomAttribute("Tracker started plan", this.plan.getPlan_name()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadAds();
        getData();
        startPlanListAsyncTask();
        initializeView();
    }

    private void startPlanListAsyncTask() {
        new PlanWorkoutListAsyncTask().execute(new Void[0]);
    }

    private void loadAds() {
        if (FirebaseRemoteConfig.getInstance().getBoolean("workout_tracker_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_WORKOUT_TRACKER_BANNER_AD_ID);
        }
        loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
    }

    private void getData() {
        this.plan = (Plan) getIntent().getParcelableExtra("PLAN_OBJECT");
        this.isMyPlan = this.plan.getPlan_type().equals("mine");
    }

    private void initializeView() {
        getWindow().addFlags(128);
        if (this.plan != null) {
            setToolbar(this.plan.getPlan_name(), true);
        } else {
            setToolbar(AppConstants.TABLE_NAME_WORKOUT, true);
        }
        initialiseMediaPlayers();
        getAndSetAllViews();
    }

    private void initialiseMediaPlayers() {
        this.mediaplayerTickSound = MediaPlayer.create(this, R.raw.tick_sound);
        this.mediaPlayerThreeTwoOneGoSound = MediaPlayer.create(this, R.raw.three_two_one_go);
    }

    private void getAndSetAllViews() {
        this.relativeLayoutWorkoutExercise = (RelativeLayout) findViewById(R.id.relativeLayoutWorkoutExercise);
        this.cardViewVideo = (CardView) findViewById(R.id.cardViewVideo);
        this.cardViewGetReady = (CardView) findViewById(R.id.cardViewGetReady);
        this.imageViewWorkoutExercise = (ImageView) findViewById(R.id.imageViewWorkoutExercise);
        this.floatingButtonPlayPause = (FloatingActionButton) findViewById(R.id.floatingButtonPlayPause);
        FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.floatingNextButton);
        this.progressBarWorkoutExerciseImage = (ProgressBar) findViewById(R.id.progressBarWorkoutExerciseImage);
        this.progressBarVideo = (ProgressBar) findViewById(R.id.progressBarVideo);
        this.textViewWorkoutExerciseSetsAndReps = (TextView) findViewById(R.id.textViewWorkoutExerciseSetsAndReps);
        this.textViewWorkoutExerciseName = (TextView) findViewById(R.id.textViewWorkoutExerciseName);
        this.textViewSet = (TextView) findViewById(R.id.textViewSet);
        this.textViewRep = (TextView) findViewById(R.id.textViewRep);
        this.textViewLoad = (TextView) findViewById(R.id.textViewLoad);
        this.textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
        this.textViewGetReady = (TextView) findViewById(R.id.textViewGetReady);
        this.textViewExerciseNumber = (TextView) findViewById(R.id.textViewExerciseNumber);
        this.textViewExerciseName = (TextView) findViewById(R.id.textViewExerciseName);
        this.textViewFirstExercise = (TextView) findViewById(R.id.textViewFirstExercise);
        this.donutProgressTimer = (DonutProgress) findViewById(R.id.donutProgressTimer);
        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.floatingButtonPlayPause.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        startGetReadyTextAnimation();
        this.cardViewVideo.setVisibility(View.GONE);
        this.relativeLayoutWorkoutExercise.setVisibility(View.VISIBLE);
        this.textViewSet.setVisibility(View.GONE);
        this.textViewRep.setVisibility(View.GONE);
        this.textViewLoad.setVisibility(View.GONE);
        this.textViewSpeed.setVisibility(View.GONE);
    }

    private void startGetReadyTextAnimation() {
        float[] from = new float[3];
        float[] to = new float[3];
        Color.colorToHSV(Color.parseColor("#D69345"), from);
        Color.colorToHSV(Color.parseColor("#ffffff"), to);
        this.valueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.valueAnimator.setInterpolator(new LinearInterpolator());
        this.valueAnimator.setRepeatCount(-1);
        this.valueAnimator.setDuration(1000);
        final float[] hsv = new float[3];
        final float[] finalTo = to;
        final float[] finalFrom = from;
        this.valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                hsv[0] = finalFrom[0] + ((finalTo[0] - finalFrom[0]) * animation.getAnimatedFraction());
                hsv[1] = finalFrom[1] + ((finalTo[1] - finalFrom[1]) * animation.getAnimatedFraction());
                hsv[2] = finalFrom[2] + ((finalTo[2] - finalFrom[2]) * animation.getAnimatedFraction());
                WorkoutTrackerStart.this.cardViewGetReady.setCardBackgroundColor(Color.HSVToColor(hsv));
            }
        });
        this.valueAnimator.start();
        this.cardViewGetReady.invalidate();
    }

    private void initializeEverything() {
        this.isRestingState = true;
        this.isPausedState = false;
        changeGetReadyTextforStartingState();
        changePlayButtonStateByPausePlayState();
        this.totalExercises = this.planWorkoutList.size();
        this.currentPosition = 0;
        this.currentSet = 0;
        if (!(AppUtil.isCollectionEmpty(this.planWorkoutList) || this.planWorkoutList.get(this.currentPosition) == null)) {
            updateExtract();
            setupWorkoutRowView();
        }
        this.donutProgressTimer.setFinishedStrokeColor(Color.parseColor("#D69345"));
        this.donutProgressTimer.setMax(this.getReadyTimer);
        Log.w("donutProgressTimer", "donutProgressTimer setting max = " + this.getReadyTimer);
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
        this.countDownTimer = new MyCountDownTimer((long) (this.getReadyTimer * 1000), 1000, true);
        this.countDownTimer.create();
    }

    private void changePlayButtonStateByPausePlayState() {
        if (this.isPausedState) {
            this.floatingButtonPlayPause.setImageResource(R.drawable.tracker_play);
        } else {
            this.floatingButtonPlayPause.setImageResource(R.drawable.tracker_pause);
        }
    }

    private void changeGetReadyTextforPause() {
        if (this.isPausedState) {
            this.textViewGetReady.setText("Paused");
        } else {
            this.textViewGetReady.setText("Workout!");
        }
    }

    private void changeGetReadyTextforStartingState() {
        if (this.isPausedState) {
            this.textViewGetReady.setText("Paused");
        } else {
            this.textViewGetReady.setText("Get Ready");
        }
    }

    private void ChangeGetReadyTextforRestState() {
        if (this.isPausedState) {
            this.textViewGetReady.setText("Paused");
        } else {
            this.textViewGetReady.setText("Rest");
        }
    }

    public void onPause() {
        super.onPause();
        pauseTracker();
    }

    private void pauseTracker() {
        if (this.valueAnimator != null) {
            this.valueAnimator.cancel();
        }
        this.isPausedState = true;
        if (!this.isRestingState) {
            changePlayButtonStateByPausePlayState();
            if (this.videoView != null) {
                this.videoView.pause();
            }
        }
        changeGetReadyTextforPause();
        if (this.countDownTimer != null) {
            this.countDownTimer.pause();
        }
        if (this.mediaPlayerThreeTwoOneGoSound != null && this.mediaPlayerThreeTwoOneGoSound.isPlaying()) {
            this.mediaPlayerThreeTwoOneGoSound.pause();
            this.isThreeTwoOneGoSoundPausedState = true;
        }
        if (this.mediaplayerTickSound != null && this.mediaplayerTickSound.isPlaying()) {
            this.mediaplayerTickSound.pause();
            this.isTickSoundPausedState = true;
        }
        changePlayButtonStateByPausePlayState();
        if (this.isGetReadyState) {
            changeGetReadyTextforStartingState();
        }
    }

    private void resumeTracker() {
        if (this.valueAnimator != null) {
            this.valueAnimator.start();
        }
        this.isPausedState = false;
        if (!(this.isRestingState || this.videoView == null)) {
            this.videoView.start();
        }
        if (this.countDownTimer != null) {
            this.countDownTimer.resume();
        }
        if (this.mediaPlayerThreeTwoOneGoSound != null && this.isThreeTwoOneGoSoundPausedState) {
            this.mediaPlayerThreeTwoOneGoSound.start();
        }
        if (this.mediaplayerTickSound != null && this.isTickSoundPausedState) {
            this.mediaplayerTickSound.start();
        }
        this.isTickSoundPausedState = false;
        this.isThreeTwoOneGoSoundPausedState = false;
        changePlayButtonStateByPausePlayState();
        if (this.isRestingState) {
            ChangeGetReadyTextforRestState();
        } else {
            changeGetReadyTextforPause();
        }
        if (this.isGetReadyState) {
            changeGetReadyTextforStartingState();
        }
    }

    private void handlePlay_PauseButton() {
        if (this.isPausedState) {
            resumeTracker();
        } else {
            pauseTracker();
        }
    }

    private void handleNextButton() {
        this.isPausedState = false;
        this.isRestingState = false;
        this.isGetReadyState = false;
        if (this.mediaPlayerThreeTwoOneGoSound != null) {
            this.mediaPlayerThreeTwoOneGoSound.seekTo(0);
            this.mediaPlayerThreeTwoOneGoSound.pause();
        }
        this.isTickSoundPausedState = false;
        this.isThreeTwoOneGoSoundPausedState = false;
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
        this.currentPosition++;
        changePlayButtonStateByPausePlayState();
        if (this.currentPosition < this.totalExercises) {
            this.currentSet = 0;
            updateExtract();
            if (this.mediaPlayerThreeTwoOneGoSound != null && this.mediaPlayerThreeTwoOneGoSound.isPlaying()) {
                this.mediaPlayerThreeTwoOneGoSound.pause();
                this.isThreeTwoOneGoSoundPausedState = true;
            }
            startExercising();
            return;
        }
        startStatsActivity();
    }

    private void updateTrackerToNextSetOrExerciseOrEndTheTracker() {
        if (this.isRestingState) {
            this.currentSet++;
        }
        if (this.currentSet < this.sets) {
            startExerciseOrResting();
            return;
        }
        this.currentPosition++;
        if (this.currentPosition < this.totalExercises) {
            this.currentSet = 0;
            startExerciseOrResting();
            return;
        }
        startStatsActivity();
    }

    private void updateExtract() {
        try {
            this.currentWorkout = (Workout) this.workoutList.get(this.currentPosition);
            this.currentPlan = (Plan) this.planWorkoutList.get(this.currentPosition);
            extractPlanData(((Plan) this.planWorkoutList.get(this.currentPosition)).getSets_and_reps());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Tracker EXCEPTION"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            finish();
        }
        String repsString = "";
        for (int i = 0; i < this.sets; i++) {
            repsString = repsString + this.repsArray[i] + HelpFormatter.DEFAULT_OPT_PREFIX;
        }
        this.currentSetsAndRepsString = "Reps: " + repsString.substring(0, repsString.length() - 1) + "\nLoad: " + this.load + " \nSpeed: " + this.speed + " \nRest: " + this.rest + "s";
        if (this.currentPlan != null) {
            this.currentWorkoutName = this.currentPlan.getWorkout_name();
        }
    }

    private void startExerciseOrResting() {
        updateExtract();
        if (this.isRestingState) {
            startResting();
        } else {
            startExercising();
        }
    }

    private void startExercising() {
        this.isGetReadyState = false;
        this.isRestingState = false;
        this.isPausedState = false;
        changePlayButtonStateByPausePlayState();
        this.textViewSet.setVisibility(View.VISIBLE);
        this.textViewRep.setVisibility(View.VISIBLE);
        this.textViewLoad.setVisibility(View.VISIBLE);
        this.textViewSpeed.setVisibility(View.VISIBLE);
        this.relativeLayoutWorkoutExercise.setVisibility(View.GONE);
        this.textViewSet.setText("Set: " + (this.currentSet + 1) + "/" + this.sets);
        this.textViewRep.setText("Reps: " + this.repsArray[this.currentSet]);
        this.textViewLoad.setText("Load: " + this.load);
        this.textViewSpeed.setText("Speed: " + this.speed);
        this.textViewGetReady.setText("Workout!");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.cardViewVideo.getLayoutParams().height = (int) (((double) displayMetrics.heightPixels) / 2.3d);
        this.cardViewVideo.requestLayout();
        this.cardViewVideo.setVisibility(View.VISIBLE);
        setUpVideoView();
    }

    private void startResting() {
        this.isRestingState = true;
        changePlayButtonStateByPausePlayState();
        this.videoView.stopPlayback();
        this.textViewSet.setVisibility(View.GONE);
        this.textViewRep.setVisibility(View.GONE);
        this.textViewLoad.setVisibility(View.GONE);
        this.textViewSpeed.setVisibility(View.GONE);
        this.textViewGetReady.setText("REST");
        this.textViewFirstExercise.setText("NEXT EXERCISE");
        this.cardViewVideo.setVisibility(View.GONE);
        this.relativeLayoutWorkoutExercise.setVisibility(View.VISIBLE);
        setupWorkoutRowView();
        this.donutProgressTimer.setMax(this.rest);
        Log.w("donutProgressTimer", "donutProgressTimer setting max = " + this.rest);
        this.donutProgressTimer.setProgress(0.0f);
        Log.w("donutProgressTimer", "donutProgressTimer setting progress to = " + String.valueOf(0));
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
        this.countDownTimer = new MyCountDownTimer((long) (this.rest * 1000), 1000, true);
        this.countDownTimer.create();
    }

    private void setupWorkoutRowView() {
        if (!(AppUtil.isCollectionEmpty(this.planWorkoutList) || this.planWorkoutList.get(this.currentPosition) == null)) {
            this.textViewWorkoutExerciseName.setText(this.currentWorkoutName);
            this.textViewWorkoutExerciseSetsAndReps.setText(this.currentSetsAndRepsString);
            this.currentWorkoutImageUrl = this.currentWorkout.getWorkout_image_name();
        }
        Resources res = getResources();
        String mDrawableName = this.currentWorkoutImageUrl;
        if (mDrawableName != null) {
            int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
            if (resID != 0) {
                Glide.with((FragmentActivity) this).load(Integer.valueOf(resID)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        WorkoutTrackerStart.this.progressBarWorkoutExerciseImage.setVisibility(View.GONE);
                        return false;
                    }

                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        WorkoutTrackerStart.this.progressBarWorkoutExerciseImage.setVisibility(View.GONE);
                        return false;
                    }
                }).into(this.imageViewWorkoutExercise);
                return;
            } else {
                this.progressBarWorkoutExerciseImage.setVisibility(View.GONE);
                return;
            }
        }
        this.progressBarWorkoutExerciseImage.setVisibility(View.GONE);
    }

    private void setUpVideoView() {
        this.textViewExerciseNumber.setText((this.currentPosition + 1) + "/" + this.totalExercises);
        this.textViewExerciseName.setText(this.currentWorkoutName);
        Resources res = getResources();
        String mDrawableName = null;
        if (this.currentWorkout != null) {
            mDrawableName = this.currentWorkout.getWorkout_video_name();
        }
        int resID = 0;
        if (mDrawableName != null) {
            resID = res.getIdentifier(mDrawableName, "raw", getPackageName());
        }
        this.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + resID));
        this.videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                WorkoutTrackerStart.this.dynamicRepTime = (int) ((((float) mediaPlayer.getDuration()) / 1000.0f) * ((float) WorkoutTrackerStart.this.repsArray[WorkoutTrackerStart.this.currentSet]));
                if (WorkoutTrackerStart.this.dynamicRepTime > SettingsJsonConstants.ANALYTICS_FLUSH_INTERVAL_SECS_DEFAULT || WorkoutTrackerStart.this.dynamicRepTime <= 2) {
                    WorkoutTrackerStart.this.dynamicRepTime = WorkoutTrackerStart.this.repsArray[WorkoutTrackerStart.this.currentSet] * 3;
                }
                WorkoutTrackerStart.this.progressBarVideo.setVisibility(View.GONE);
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0.0f, 0.0f);
                WorkoutTrackerStart.this.videoView.seekTo(0);
                WorkoutTrackerStart.this.videoView.pause();
                if (!WorkoutTrackerStart.this.isRestingState && !WorkoutTrackerStart.this.isPausedState) {
                    WorkoutTrackerStart.this.donutProgressTimer.setMax(WorkoutTrackerStart.this.dynamicRepTime);
                    Log.w("donutProgressTimer", "donutProgressTimer setting max to = " + WorkoutTrackerStart.this.dynamicRepTime);
                    WorkoutTrackerStart.this.donutProgressTimer.setProgress(0.0f);
                    Log.w("donutProgressTimer", "donutProgressTimer setting progress to = 0");
                    WorkoutTrackerStart.this.videoView.start();
                    if (WorkoutTrackerStart.this.countDownTimer != null) {
                        WorkoutTrackerStart.this.countDownTimer.cancel();
                    }
                    WorkoutTrackerStart.this.countDownTimer = new MyCountDownTimer((long) (WorkoutTrackerStart.this.dynamicRepTime * 1000), 1000, true);
                    WorkoutTrackerStart.this.countDownTimer.create();
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingButtonPlayPause:
                handlePlay_PauseButton();
                return;
            case R.id.floatingNextButton:
                handleNextButton();
                return;
            default:
                return;
        }
    }

    private void extractPlanData(String execiseDescriptionContainingAllData) {
        try {
            if (this.isMyPlan) {
                extractDataForCustomPlan(execiseDescriptionContainingAllData);
            } else {
                extractDataForNormalPlan(execiseDescriptionContainingAllData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            initializeDefaults();
        }
    }

    private void extractDataForCustomPlan(String str) {
        initializeDefaults();
        initializeCustomSets(str);
        initializzeCustomReps(str);
    }

    private void initializzeCustomReps(String str) {
        this.repsArray = getReps(str);
    }

    private void initializeCustomSets(String str) {
        this.sets = getSets(str);
    }

    private void extractDataForNormalPlan(String S1) {
        if (S1 == null) {
            initializeDefaults();
            return;
        }
        try {
            String[] splitted = S1.split("\\s+");
            if (splitted.length != 8) {
                initializeDefaults();
                return;
            }
            try {
                this.sets = getSets(splitted[1]);
            } catch (Exception e) {
                this.sets = 4;
                e.printStackTrace();
            }
            try {
                this.repsArray = getReps(splitted[1]);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                this.load = getLoad(splitted[3]);
            } catch (Exception e22) {
                initializeDefaultLOAD();
                e22.printStackTrace();
            }
            try {
                this.speed = getSpeed(splitted[5]);
            } catch (Exception e222) {
                initializeDefaultSPEED();
                e222.printStackTrace();
            }
            try {
                this.rest = getRest(splitted[7]);
            } catch (Exception e2222) {
                initializeDefaultREST();
                e2222.printStackTrace();
            }
        } catch (Exception e22222) {
            initializeDefaults();
            e22222.printStackTrace();
        }
    }

    private void initializeDefaultREST() {
        this.rest = 60;
    }

    private void initializeDefaultSPEED() {
        this.speed = "70%";
    }

    private void initializeDefaultLOAD() {
        this.load = "Normal";
    }

    private void initializeDefaultREPS() {
        this.exerciseTimeArray = new int[]{30, 30, 30, 30};
        this.repsArray = new int[]{10, 10, 10, 10};
    }

    private void initializeDefaultSETS() {
        this.sets = 4;
    }

    private void initializeDefaults() {
        initializeDefaultLOAD();
        initializeDefaultREPS();
        initializeDefaultREST();
        initializeDefaultSETS();
        initializeDefaultSPEED();
    }

    private int getRest(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 's') {
            str = str.substring(0, str.length() - 1);
        }
        return Integer.parseInt(str);
    }

    private String getSpeed(String str) {
        return str;
    }

    private String getLoad(String str) {
        return str;
    }

    private int[] getReps(String str) {
        int i;
        str = str.replaceAll("\\s+", "");
        String[] splits;
        int[] rt;
        if (str.contains("s")) {
            this.exerciseTimeArray = new int[getSets(str)];
            splits = str.split("s");
            rt = new int[getSets(str)];
            i = 0;
            while (i < getSets(str)) {
                try {
                    rt[i] = Math.abs(Integer.parseInt(splits[i]));
                    this.exerciseTimeArray[i] = rt[i] * 3;
                    i++;
                } catch (Exception e) {
                    for (i = 0; i < getSets(str); i++) {
                        rt[i] = 10;
                        this.exerciseTimeArray[i] = rt[i] * 3;
                    }
                    e.printStackTrace();
                }
            }
            return rt;
        }
        this.exerciseTimeArray = new int[getSets(str)];
        splits = str.split(HelpFormatter.DEFAULT_OPT_PREFIX);
        rt = new int[getSets(str)];
        i = 0;
        while (i < getSets(str)) {
            try {
                rt[i] = Math.abs(Integer.parseInt(splits[i]));
                this.exerciseTimeArray[i] = rt[i] * 3;
                i++;
            } catch (Exception e2) {
                for (i = 0; i < getSets(str); i++) {
                    rt[i] = 10;
                    this.exerciseTimeArray[i] = rt[i] * 3;
                }
                e2.printStackTrace();
            }
        }
        return rt;
    }

    private int getSets(String str) {
        try {
            return str.replaceAll("\\s+", "").split(HelpFormatter.DEFAULT_OPT_PREFIX).length;
        } catch (Exception e) {
            e.printStackTrace();
            return 4;
        }
    }

    private void prepareWorkoutVideosList() {
        Iterator it = this.planWorkoutList.iterator();
        while (it.hasNext()) {
            this.workoutList.add(DatabaseHelper.getInstance(this).getWorkout(((Plan) it.next()).getWorkout_id()));
        }
    }

    public void preparePlanWorkoutList() {
        ArrayList<Plan> workoutArrayList;
        if (this.isMyPlan) {
            workoutArrayList = DatabaseHelper.getInstance(this).getWorkoutListForMyPlanDay(this.plan);
        } else {
            workoutArrayList = DatabaseHelper.getInstance(this).getWorkoutListForPlanDay(this.plan);
        }
        this.planWorkoutList.clear();
        this.planWorkoutList.addAll(workoutArrayList);
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this).setTitle("Exit Workout?").setMessage("Are you want to exit? Progress will be lost").setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (WorkoutTrackerStart.this.interstitialAd == null || !WorkoutTrackerStart.this.interstitialAd.isLoaded()) {
                    WorkoutTrackerStart.this.handleTrackerBack();
                    return;
                }
                WorkoutTrackerStart.this.showInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
                WorkoutTrackerStart.this.interstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        WorkoutTrackerStart.this.loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
                        WorkoutTrackerStart.this.handleTrackerBack();
                    }
                });
            }
        }).setNegativeButton("No", null).create().show();
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

    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void handleTrackerBack() {
        finish();
    }

    private void startStatsActivity() {
        String duration;
        try {
            long s = (System.currentTimeMillis() - this.startTime) / 1000;
            if (s > 60) {
                duration = String.valueOf((int) (s / 60)) + " min";
            } else {
                duration = String.valueOf((int) s) + " sec";
            }
        } catch (Exception e) {
            e.printStackTrace();
            duration = EnvironmentCompat.MEDIA_UNKNOWN;
        }
        Toast.makeText(this, "Workout Completed", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, TrackerStatsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("duration", duration);
        bundle.putParcelable("PLAN_OBJECT", this.plan);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
