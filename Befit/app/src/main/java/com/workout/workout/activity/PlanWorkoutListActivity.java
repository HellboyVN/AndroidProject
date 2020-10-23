package com.workout.workout.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.adapter.PlanWorkoutListAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.NativeAdsTaskManager;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.Workout;
import com.workout.workout.util.AppUtil;
import com.workout.workout.util.SimpleDividerItemDecoration;
import java.util.ArrayList;

public class PlanWorkoutListActivity extends BaseActivity implements OnListFragmentInteractionListener {
    public static final int NATIVE_APP_MAX_WIDTH = 1200;
    private static int availableWidthForNativeAds;
    private boolean isMyPlan;
    private Plan plan;
    private PlanWorkoutListAdapter planWorkoutListAdapter;
    private FloatingActionButton startTrackerButton;
    private TextView textViewEmpty;
    private ArrayList<Plan> workoutList = new ArrayList();

    private class PlanWorkoutListAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlanWorkoutListAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            PlanWorkoutListActivity.this.preparePlanWorkoutList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PlanWorkoutListActivity.this.planWorkoutListAdapter.setRowItemList(NativeAdsTaskManager.prepareNativeAdsInPlanWorkoutRowItemList(PlanWorkoutListActivity.this, PlanWorkoutListActivity.this.workoutList));
            PlanWorkoutListActivity.this.planWorkoutListAdapter.notifyDataSetChanged();
            if (PlanWorkoutListActivity.this.workoutList.size() == 0) {
                PlanWorkoutListActivity.this.textViewEmpty.setVisibility(View.VISIBLE);
            } else {
                PlanWorkoutListActivity.this.textViewEmpty.setVisibility(View.GONE);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_plan_workout_list);
        availableWidthForNativeAds = calculateAvailableWidthForNativeAdView();
        if (FirebaseRemoteConfig.getInstance().getBoolean("plan_workout_list_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_PLAN_WORKOUT_LIST_BANNER_AD_ID);
        }
        loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
        getData();
        initializeView();
    }

    private void getData() {
        this.plan = (Plan) getIntent().getParcelableExtra("PLAN_OBJECT");
        this.isMyPlan = this.plan.getPlan_type().equals("mine");
    }

    private void initializeView() {
        setToolbar(this.plan.getDay(), true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        this.planWorkoutListAdapter = new PlanWorkoutListAdapter(this, this);
        recyclerView.setAdapter(this.planWorkoutListAdapter);
        initializeTracker();
    }

    private void initializeTracker() {
        this.startTrackerButton = (FloatingActionButton) findViewById(R.id.floatingStartButton);
        this.startTrackerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Start Tracker from inside Days clicked"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(PlanWorkoutListActivity.this, WorkoutTrackerStart.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("PLAN_OBJECT", PlanWorkoutListActivity.this.plan);
                intent.putExtras(bundle);
                PlanWorkoutListActivity.this.startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        new PlanWorkoutListAsyncTask().execute(new Void[0]);
        setupWorkoutTracker();
    }

    private void setupWorkoutTracker() {
        if (this.startTrackerButton != null) {
            this.startTrackerButton.setVisibility(View.GONE);
            try {
                String count;
                if (this.plan.getPlan_type().equals("mine")) {
                    count = DatabaseHelper.getInstance(this).getMyPlanExercisesCount(this.plan);
                } else {
                    count = DatabaseHelper.getInstance(this).getExercisesCount(this.plan);
                }
                if (count.equalsIgnoreCase("0")) {
                    this.startTrackerButton.setVisibility(View.GONE);
                } else {
                    this.startTrackerButton.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.startTrackerButton.setVisibility(View.GONE);
            }
        }
    }

    private void preparePlanWorkoutList() {
        ArrayList<Plan> workoutArrayList;
        if (this.isMyPlan) {
            workoutArrayList = DatabaseHelper.getInstance(this).getWorkoutListForMyPlanDay(this.plan);
        } else {
            workoutArrayList = DatabaseHelper.getInstance(this).getWorkoutListForPlanDay(this.plan);
        }
        this.workoutList.clear();
        this.workoutList.addAll(workoutArrayList);
    }

    public void onListFragmentInteraction(final BaseModel model, int position) {
        if (model instanceof Plan) {
            boolean showInterstitialAd;
            if (((long) PersistenceManager.getWorkoutDetailActivityOpenCount()) % ((long) PersistenceManager.getWorkout_detail_interstitial_ads_interval()) == 0) {
                showInterstitialAd = true;
            } else {
                showInterstitialAd = false;
            }
            if (this.interstitialAd != null && this.interstitialAd.isLoaded() && showInterstitialAd) {
                showInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
                this.interstitialAd.setAdListener(new AdListener() {
                    public void onAdClosed() {
                        super.onAdClosed();
                        PlanWorkoutListActivity.this.loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
                        Workout workout = DatabaseHelper.getInstance(PlanWorkoutListActivity.this).getWorkout(((Plan) model).getWorkout_id());
                        Intent intent = new Intent(PlanWorkoutListActivity.this, WorkoutDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(AppConstants.WORKOUT_OBJECT, workout);
                        intent.putExtras(bundle);
                        PlanWorkoutListActivity.this.startActivity(intent);
                    }
                });
                return;
            }
            Workout workout = DatabaseHelper.getInstance(this).getWorkout(((Plan) model).getWorkout_id());
            Intent intent = new Intent(this, WorkoutDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppConstants.WORKOUT_OBJECT, workout);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case R.id.add:
                Intent intent = new Intent(this, AddWorkoutToPlanActivity.class);
                intent.putExtra("PLAN_OBJECT", this.plan);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.plan.getPlan_type().equals("mine")) {
            getMenuInflater().inflate(R.menu.my_plan_menu, menu);
        }
        return true;
    }

    public void deleteWorkout(Plan plan) {
        DatabaseHelper.getInstance(this).deleteWorkoutFromPlan(plan, DatabaseHelper.getInstance(this).getWorkout(plan.getWorkout_id()));
        setupWorkoutTracker();
        new PlanWorkoutListAsyncTask().execute(new Void[0]);
    }

    public int getAvailableWidthForNativeAds() {
        return availableWidthForNativeAds;
    }

    public int getAvailableHeightForNativeAds() {
        return 140;
    }

    public int calculateAvailableWidthForNativeAdView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidthInDp = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        if (deviceWidthInDp > 1200) {
            deviceWidthInDp = 1200;
        }
        return (deviceWidthInDp - ((int) AppUtil.getDimenstionInDp(this, R.dimen.plan_workout_list_native_ads_margin))) - ((int) AppUtil.getDimenstionInDp(this, R.dimen.plan_workout_list_native_ads_margin));
    }
}
