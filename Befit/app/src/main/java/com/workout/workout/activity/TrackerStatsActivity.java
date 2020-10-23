package com.workout.workout.activity;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.ads.AdListener;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.Workout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TrackerStatsActivity extends BaseActivity {
    private Button buttonExit;
    private String duration;
    private ImageView imageViewPlan;
    private boolean isMyPlan;
    private Plan plan;
    private ArrayList<Plan> planWorkoutList = new ArrayList();
    private ProgressBar progressBarImageView;
    private TextView textViewDuration;
    private TextView textViewMuscleFocussed;
    private TextView textViewPlanDay;
    private TextView textViewPlanName;
    private TextView textViewTotalExercises;
    private ArrayList<Workout> workoutList = new ArrayList();

    private class PlanWorkoutListAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlanWorkoutListAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            TrackerStatsActivity.this.preparePlanWorkoutList();
            TrackerStatsActivity.this.prepareWorkoutVideosList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TrackerStatsActivity.this.setAllViews();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_tracker_stats);
        try {
            Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Tracker Stats page reached").putCustomAttribute("Tracker stats plan", this.plan.getPlan_name()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadAds();
        try {
            getData();
        } catch (Exception e2) {
            e2.printStackTrace();
            finish();
        }
        initializeView();
        startPlanListAsyncTask();
    }

    private void setAllViews() {
        Resources res = getResources();
        String mDrawableName = null;
        int resID = 0;
        if (this.plan.getBody_part_image_url() != null && !this.plan.getBody_part_image_url().isEmpty()) {
            mDrawableName = this.plan.getBody_part_image_url();
        } else if (this.plan.getPlan_image_name() != null) {
            mDrawableName = this.plan.getPlan_image_name();
        }
        if (mDrawableName == null) {
            resID = R.mipmap.ic_launcher;
        }
        if (mDrawableName != null) {
            resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
        }
        if (resID != 0) {
            Glide.with((FragmentActivity) this).load(Integer.valueOf(resID)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
                public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                    TrackerStatsActivity.this.progressBarImageView.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    TrackerStatsActivity.this.progressBarImageView.setVisibility(View.GONE);
                    return false;
                }
            }).into(this.imageViewPlan);
        } else {
            this.progressBarImageView.setVisibility(View.GONE);
        }
        this.buttonExit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TrackerStatsActivity.this.onBackPressed();
            }
        });
        try {
            this.textViewPlanDay.setText(this.plan.getDay() + "");
        } catch (Exception e) {
            e.printStackTrace();
            this.textViewPlanDay.setText("Unknown");
        }
        try {
            this.textViewPlanName.setText(this.plan.getPlan_name() + "");
        } catch (Exception e2) {
            e2.printStackTrace();
            this.textViewPlanName.setText("Unknown");
        }
        try {
            this.textViewDuration.setText(this.duration + "");
        } catch (Exception e22) {
            e22.printStackTrace();
            this.textViewDuration.setText("Unknown");
        }
        try {
            this.textViewTotalExercises.setText(this.planWorkoutList.size() + "");
        } catch (Exception e222) {
            e222.printStackTrace();
            this.textViewTotalExercises.setText("Unknown");
        }
        try {
            String[] musclearray = new String[this.workoutList.size()];
            for (int i = 0; i < this.workoutList.size(); i++) {
                musclearray[i] = ((Workout) this.workoutList.get(i)).getBody_part_name();
            }
            this.textViewMuscleFocussed.setText(majorMuscle(musclearray));
        } catch (Exception e2222) {
            e2222.printStackTrace();
            this.textViewMuscleFocussed.setText("Muscles");
        }
    }

    private void initializeView() {
        this.imageViewPlan = (ImageView) findViewById(R.id.imageViewPlan);
        this.textViewPlanName = (TextView) findViewById(R.id.textViewPlanName);
        this.textViewPlanDay = (TextView) findViewById(R.id.textViewPlanDay);
        this.textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        this.textViewMuscleFocussed = (TextView) findViewById(R.id.textViewMuscleFocussed);
        this.textViewTotalExercises = (TextView) findViewById(R.id.textViewTotalExercises);
        this.progressBarImageView = (ProgressBar) findViewById(R.id.progressBarImageView);
        this.buttonExit = (Button) findViewById(R.id.buttonExit);
        this.progressBarImageView.setVisibility(View.VISIBLE);
    }

    private void loadAds() {
        loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
    }

    private void startPlanListAsyncTask() {
        new PlanWorkoutListAsyncTask().execute(new Void[0]);
    }

    private void getData() {
        this.plan = (Plan) getIntent().getParcelableExtra("PLAN_OBJECT");
        if (this.plan != null) {
            this.isMyPlan = this.plan.getPlan_type().equals("mine");
        }
        Bundle b = getIntent().getExtras();
        if (b != null) {
            this.duration = b.getString("duration");
        }
    }

    private void preparePlanWorkoutList() {
        ArrayList<Plan> workoutArrayList;
        if (this.isMyPlan) {
            workoutArrayList = DatabaseHelper.getInstance(this).getWorkoutListForMyPlanDay(this.plan);
        } else {
            workoutArrayList = DatabaseHelper.getInstance(this).getWorkoutListForPlanDay(this.plan);
        }
        this.planWorkoutList.clear();
        this.planWorkoutList.addAll(workoutArrayList);
    }

    private void prepareWorkoutVideosList() {
        Iterator it = this.planWorkoutList.iterator();
        while (it.hasNext()) {
            this.workoutList.add(DatabaseHelper.getInstance(this).getWorkout(((Plan) it.next()).getWorkout_id()));
        }
    }

    public void onBackPressed() {
        if (this.interstitialAd == null || !this.interstitialAd.isLoaded()) {
            handleTrackerBack();
            return;
        }
        showInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
        this.interstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                super.onAdClosed();
                TrackerStatsActivity.this.loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
                TrackerStatsActivity.this.handleTrackerBack();
            }
        });
    }

    private void handleTrackerBack() {
        finish();
    }

    public String majorMuscle(String[] array) {
        List<String> list = Arrays.asList(array);
        Map<String, Integer> stringsCount = new HashMap();
        for (String string : list) {
            String string2 = null;
            if (string2.length() > 0) {
                string2 = string2.toLowerCase();
                Integer count = (Integer) stringsCount.get(string2);
                if (count == null) {
                    count = Integer.valueOf(0);
                }
                stringsCount.put(string2, Integer.valueOf(count.intValue() + 1));
            }
        }
        Entry<String, Integer> mostRepeated = null;
        for (Entry<String, Integer> e : stringsCount.entrySet()) {
            if (mostRepeated == null || ((Integer) mostRepeated.getValue()).intValue() < ((Integer) e.getValue()).intValue()) {
                mostRepeated = e;
            }
        }
        try {
            return (String) mostRepeated.getKey();
        } catch (NullPointerException e2) {
            return "Muscles";
        }
    }
}
