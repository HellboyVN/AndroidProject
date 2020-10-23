package com.workout.workout.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.database.DatabaseManager;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.Workout;

public class WorkoutDetailActivity extends BaseActivity implements OnClickListener {
    private ImageView imageViewAddToPlan;
    private ImageView imageViewLike;
    private FirebaseAnalytics mFirebaseAnalytics;
    private ProgressBar progressBar;
    private TextView textViewBodyPart;
    private TextView textViewDescription;
    private TextView textViewWorkout;
    private VideoView videoView;
    private Workout workout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_workout_detail);
        if (FirebaseRemoteConfig.getInstance().getBoolean("workout_detail_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_WORKOUT_DETAIL_BANNER_AD_ID);
        }
        getData();
        initializeView();
    }

    private void getData() {
        Workout workout = (Workout) getIntent().getExtras().getParcelable(AppConstants.WORKOUT_OBJECT);
        if (workout != null) {
            this.workout = DatabaseHelper.getInstance(this).getWorkout(workout.getWorkout_id());
        }
    }

    private void initializeView() {
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        CardView videoCardView = (CardView) findViewById(R.id.videoCarView);
        videoCardView.getLayoutParams().height = (int) (((double) displayMetrics.heightPixels) / 2.3d);
        videoCardView.requestLayout();
        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        this.imageViewLike = (ImageView) findViewById(R.id.imageViewLike);
        this.textViewBodyPart = (TextView) findViewById(R.id.textViewBodyPart);
        this.textViewWorkout = (TextView) findViewById(R.id.textViewWorkout);
        this.imageViewAddToPlan = (ImageView) findViewById(R.id.imageViewAddToPlan);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.VISIBLE);
        setToolbar(this.workout.getWorkout_name(), true);
        setUpVideoView();
        this.imageViewLike.setOnClickListener(this);
        this.imageViewAddToPlan.setOnClickListener(this);
        String workoutDescription = null;
        String bodyPartName = null;
        String workoutName = null;
        if (this.workout != null) {
            workoutDescription = this.workout.getWorkout_description();
            bodyPartName = this.workout.getBody_part_name();
            workoutName = this.workout.getWorkout_name();
        }
        if (workoutDescription != null) {
            workoutDescription = workoutDescription.replace("\\n ", System.getProperty("line.separator")).replace("\\n  ", System.getProperty("line.separator")).replace("\\n", System.getProperty("line.separator"));
        }
        this.textViewDescription.setText(workoutDescription);
        this.textViewBodyPart.setText(bodyPartName);
        this.textViewWorkout.setText(workoutName);
        try {
            Answers.getInstance().logContentView((ContentViewEvent) ((ContentViewEvent) new ContentViewEvent().putContentName("Workout Detail Activity").putCustomAttribute("Exercise name", workoutName)).putCustomAttribute("BodyPart name", bodyPartName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle bundle1 = new Bundle();
        bundle1.putString("body_part_name", bodyPartName);
        bundle1.putString("workout_name", workoutName);
        this.mFirebaseAnalytics.logEvent(Event.VIEW_ITEM, bundle1);
    }

    public void onResume() {
        super.onResume();
        if (DatabaseManager.getInstance(this).isFavouriteWorkout(this.workout.getWorkout_id())) {
            this.imageViewLike.setImageResource(R.drawable.like_selected);
        } else {
            this.imageViewLike.setImageResource(R.drawable.like_unselected);
        }
    }

    private void setUpVideoView() {
        Resources res = getResources();
        String mDrawableName = this.workout.getWorkout_video_name();
        int resID = 0;
        if (mDrawableName != null) {
            resID = res.getIdentifier(mDrawableName, "raw", getPackageName());
        }
        this.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + resID));
        this.videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                WorkoutDetailActivity.this.progressBar.setVisibility(View.GONE);
                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(0.0f, 0.0f);
                WorkoutDetailActivity.this.videoView.start();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewLike:
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Like button pressed").putCustomAttribute("Liked exercise name", this.workout.getWorkout_name()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handleImageViewLikeClick();
                return;
            case R.id.imageViewAddToPlan:
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Add to plan button pressed").putCustomAttribute("Add to plan exercise name", this.workout.getWorkout_name()));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                handleImageViewAddToPlan();
                return;
            default:
                return;
        }
    }

    private void handleImageViewLikeClick() {
        String workoutId = this.workout.getWorkout_id();
        DatabaseManager databaseManager = DatabaseManager.getInstance(this);
        if (databaseManager.isFavouriteWorkout(workoutId)) {
            databaseManager.removeWorkoutFromFavourite(workoutId);
            this.imageViewLike.setImageResource(R.drawable.like_unselected);
            return;
        }
        databaseManager.addWorkout(this.workout);
        this.imageViewLike.setImageResource(R.drawable.like_selected);
    }

    private void handleImageViewAddToPlan() {
        Plan plan = new Plan();
        Intent intent = new Intent(this, AddWorkoutToPlanActivity.class);
        intent.putExtra(AppConstants.WORKOUT_OBJECT, this.workout);
        intent.putExtra("PLAN_OBJECT", plan);
        startActivity(intent);
    }
}
