package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.adapter.PlanWorkoutListAdapter;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.constant.AppConstants;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.database.DatabaseHelper;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.listener.OnListFragmentInteractionListener;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.NativeAdsTaskManager;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.BaseModel;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Plan;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.modal.Workout;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.AppUtil;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.util.SimpleDividerItemDecoration;

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
        loadBannerAdvertisement(this);
        Random rand = new Random();
        int n = rand.nextInt(4)+1;
        Log.e("levan_new","show + n= "+ String.valueOf(n));
        if(n==2){
            AppConstants.showFAd(this);
        }
        availableWidthForNativeAds = calculateAvailableWidthForNativeAdView();
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
//                try {
//                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Start Tracker from inside Days clicked"));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
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
