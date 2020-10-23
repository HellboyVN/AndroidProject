package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Random;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.WorkoutListAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.NativeAdsTaskManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BaseModel;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Training;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;

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
        loadBannerAdvertisement(this);
        Random rand = new Random();
        int n = rand.nextInt(3)+1;
        Log.e("levan_new","show + n= "+ String.valueOf(n));
        if(n==2){
            AppConstants.showFAd(this);
        }
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
