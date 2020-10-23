package com.workout.workout.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.adapter.WorkoutListAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.NativeAdsTaskManager;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Training;
import com.workout.workout.modal.Workout;
import com.workout.workout.util.AppUtil;
import java.util.ArrayList;

public class TrainingDetailActivity extends BaseActivity implements OnListFragmentInteractionListener {
    public static final int NATIVE_APP_MAX_WIDTH = 1200;
    private static int availableWidthForNativeAds;
    private LinearLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private Training training;
    private ArrayList<Workout> workoutList;
    private WorkoutListAdapter workoutListAdapter;

    class WorkoutListAsyncTask extends AsyncTask<Void, Void, Void> {
        WorkoutListAsyncTask() {
        }

        protected Void doInBackground(Void... voids) {
            //evans
            TrainingDetailActivity.this.getWorkoutList();
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            TrainingDetailActivity.this.workoutListAdapter.setRowItemList(NativeAdsTaskManager.prepareNativeAdsInWorkoutRowItemList(TrainingDetailActivity.this, TrainingDetailActivity.this.workoutList));
            TrainingDetailActivity.this.workoutListAdapter.notifyDataSetChanged();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_training_detail);
        availableWidthForNativeAds = calculateAvailableWidthForNativeAdView();
        if (FirebaseRemoteConfig.getInstance().getBoolean("workout_list_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_WORKOUT_LIST_BANNER_AD_ID);
        }
        loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
        getData();
        setData();
        initializeViews();
        setAdapter();
    }

    private void initializeViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.recyclerView = (RecyclerView) findViewById(R.id.list);
        this.gridLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.gridLayoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        toolbarTextAppernce();
    }

    private void setAdapter() {
        this.workoutListAdapter = new WorkoutListAdapter(this, this, Glide.with((FragmentActivity) this));
        this.recyclerView.setAdapter(this.workoutListAdapter);
    }

    private void getData() {
        this.training = new Training();
        this.training = (Training) getIntent().getExtras().getParcelable(AppConstants.BODY_PART);
        new WorkoutListAsyncTask().execute(new Void[0]);
    }

    private void setData() {
        setToolbar(this.training.getPart_name(), true);
    }

    public void onListFragmentInteraction(final BaseModel model, int position) {
        if (model instanceof Workout) {
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
                        TrainingDetailActivity.this.loadInterstitialAds(AppConstants.ADMOB_WORKOUT_DETAIL_INTERSTITIAL_AD_ID);
                        Workout workout = (Workout) model;
                        Intent intent = new Intent(TrainingDetailActivity.this, WorkoutDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(AppConstants.WORKOUT_OBJECT, workout);
                        intent.putExtras(bundle);
                        TrainingDetailActivity.this.startActivity(intent);
                    }
                });
                return;
            }
            Workout workout = (Workout) model;
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toolbarTextAppernce() {
    }

    public void onResume() {
        super.onResume();
        if (this.workoutListAdapter != null) {
            this.workoutListAdapter.notifyDataSetChanged();
        }
    }

    private void getWorkoutList() {
        this.workoutList = DatabaseHelper.getInstance(this).getWorkoutListForBodyPart(this.training.getPart_name());
    }

    public int getAvailableWidthForNativeAds() {
        return availableWidthForNativeAds;
    }

    public int getAvailableHeightForNativeAds() {
        return 180;
    }

    public int calculateAvailableWidthForNativeAdView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidthInDp = (int) (((float) displayMetrics.widthPixels) / displayMetrics.density);
        if (deviceWidthInDp > 1200) {
            deviceWidthInDp = 1200;
        }
        return (deviceWidthInDp - ((int) AppUtil.getDimenstionInDp(this, R.dimen.native_ads_margin))) - ((int) AppUtil.getDimenstionInDp(this, R.dimen.native_ads_margin));
    }
}
