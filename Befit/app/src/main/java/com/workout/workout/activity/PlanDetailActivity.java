package com.workout.workout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.adapter.PlanDetailAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Plan;
import com.workout.workout.util.SimpleDividerItemDecoration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlanDetailActivity extends BaseActivity implements OnListFragmentInteractionListener {
    String dayOfTheWeek;
    private Plan plan;
    private ArrayList<Plan> planList = new ArrayList();
    private PlanDetailAdapter planRecyclerViewAdapter;
    private FloatingActionButton startTrackerButton;
    Plan todaysPlan;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_plan_detail);
        if (FirebaseRemoteConfig.getInstance().getBoolean("plan_day_list_banner_ads_enable")) {
            loadBannerAdvertisement(this, AppConstants.ADMOB_PLAN_DAY_LIST_BANNER_AD_ID);
        }
        getData();
        initializeView();
    }

    private void getData() {
        this.plan = (Plan) getIntent().getParcelableExtra("PLAN_OBJECT");
    }

    private void initializeView() {
        setToolbar(this.plan.getPlan_name(), true);
        RecyclerView recyclerViewPlan = (RecyclerView) findViewById(R.id.recyclerViewPlan);
        recyclerViewPlan.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPlan.addItemDecoration(new SimpleDividerItemDecoration(this));
        this.planRecyclerViewAdapter = new PlanDetailAdapter(this, this.planList, this);
        recyclerViewPlan.setAdapter(this.planRecyclerViewAdapter);
        initializeTracker();
    }

    private void initializeTracker() {
        this.startTrackerButton = (FloatingActionButton) findViewById(R.id.floatingStartButton);
        this.startTrackerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Start Tracker from outside Days clicked"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(PlanDetailActivity.this, WorkoutTrackerStart.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("PLAN_OBJECT", PlanDetailActivity.this.todaysPlan);
                intent.putExtras(bundle);
                PlanDetailActivity.this.startActivity(intent);
            }
        });
    }

    private void setupTracker() {
        if (this.startTrackerButton != null) {
            this.startTrackerButton.setVisibility(View.GONE);
            try {
                this.dayOfTheWeek = new SimpleDateFormat("EEEE").format(new Date());
                if (this.dayOfTheWeek == null) {
                    this.startTrackerButton.setVisibility(View.GONE);
                    return;
                }
                if (this.dayOfTheWeek.equalsIgnoreCase("Monday")) {
                    this.plan.setDay("Monday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Tuesday")) {
                    this.plan.setDay("Tuesday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Wednesday")) {
                    this.plan.setDay("Wednesday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Thursday")) {
                    this.plan.setDay("Thursday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Friday")) {
                    this.plan.setDay("Friday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Saturday")) {
                    this.plan.setDay("Saturday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Sunday")) {
                    this.plan.setDay("Sunday");
                } else {
                    this.startTrackerButton.setVisibility(View.GONE);
                }
                this.todaysPlan = new Plan();
                this.todaysPlan.setPlan_image_name(this.plan.getPlan_image_name());
                this.todaysPlan.setPlan_image_url(this.plan.getPlan_image_url());
                this.todaysPlan.setPlan_type(this.plan.getPlan_type());
                this.todaysPlan.setPlan_id(this.plan.getPlan_id());
                this.todaysPlan.setPlan_name(this.plan.getPlan_name());
                this.todaysPlan.setPlan_description(this.plan.getPlan_description());
                this.todaysPlan.setMyPlan(this.plan.isMyPlan());
                if (this.dayOfTheWeek.equalsIgnoreCase("Monday")) {
                    this.todaysPlan.setDay("Monday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Tuesday")) {
                    this.todaysPlan.setDay("Tuesday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Wednesday")) {
                    this.todaysPlan.setDay("Wednesday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Thursday")) {
                    this.todaysPlan.setDay("Thursday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Friday")) {
                    this.todaysPlan.setDay("Friday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Saturday")) {
                    this.todaysPlan.setDay("Saturday");
                } else if (this.dayOfTheWeek.equalsIgnoreCase("Sunday")) {
                    this.todaysPlan.setDay("Sunday");
                }
                try {
                    String count;
                    if (this.todaysPlan.getPlan_type().equals("mine")) {
                        count = DatabaseHelper.getInstance(this).getMyPlanExercisesCount(this.todaysPlan);
                    } else {
                        count = DatabaseHelper.getInstance(this).getExercisesCount(this.todaysPlan);
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
            } catch (Exception e2) {
                this.startTrackerButton.setVisibility(View.GONE);
                e2.printStackTrace();
            }
        }
    }

    public void onResume() {
        super.onResume();
        preparePlansList();
        setupTracker();
    }

    private void preparePlansList() {
        this.planList.clear();
        String planType = this.plan.getPlan_type();
        String planId = this.plan.getPlan_id();
        String planName = this.plan.getPlan_name();
        String planDescription = this.plan.getPlan_description();
        String planImageName = this.plan.getPlan_image_name();
        String planImageUrl = this.plan.getPlan_image_url();
        boolean isMyPlan = this.plan.isMyPlan();
        Plan plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Monday");
        plan.setPlan_name(planName);
        plan.setPlan_description(planDescription);
        plan.setMyPlan(isMyPlan);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Tuesday");
        plan.setMyPlan(isMyPlan);
        plan.setPlan_name(planName);
        plan.setPlan_description(planDescription);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Wednesday");
        plan.setPlan_name(planName);
        plan.setMyPlan(isMyPlan);
        plan.setPlan_description(planDescription);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Thursday");
        plan.setPlan_name(planName);
        plan.setMyPlan(isMyPlan);
        plan.setPlan_description(planDescription);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Friday");
        plan.setPlan_name(planName);
        plan.setMyPlan(isMyPlan);
        plan.setPlan_description(planDescription);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Saturday");
        plan.setPlan_name(planName);
        plan.setMyPlan(isMyPlan);
        plan.setPlan_description(planDescription);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        plan = new Plan();
        plan.setPlan_type(planType);
        plan.setPlan_id(planId);
        plan.setDay("Sunday");
        plan.setPlan_name(planName);
        plan.setMyPlan(isMyPlan);
        plan.setPlan_description(planDescription);
        plan.setPlan_image_name(planImageName);
        plan.setPlan_image_url(planImageUrl);
        this.planList.add(plan);
        this.planRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        if (model instanceof Plan) {
            Plan plan = (Plan) model;
            try {
                Answers.getInstance().logContentView((ContentViewEvent) ((ContentViewEvent) new ContentViewEvent().putContentName("Day clicked").putCustomAttribute("Day position", Integer.valueOf(position))).putCustomAttribute("day plan name", plan.getPlan_name()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, PlanWorkoutListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("PLAN_OBJECT", plan);
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
}
