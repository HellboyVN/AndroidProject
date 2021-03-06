package com.workout.workout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.workout.workout.R;
import com.workout.workout.adapter.PlanRecyclerViewAdapter;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Plan;
import com.workout.workout.util.SimpleDividerItemDecoration;
import java.util.ArrayList;

public class MyPlanListActivity extends BaseActivity implements OnListFragmentInteractionListener {
    private ArrayList<Plan> planList = new ArrayList();
    private PlanRecyclerViewAdapter planRecyclerViewAdapter;
    private RecyclerView recyclerViewPlan;
    private TextView textViewEmpty;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_my_plan_list);
        loadBannerAdvertisement(this, AppConstants.ADMOB_PLAN_DAY_LIST_BANNER_AD_ID);
        initializeView();
    }

    private void initializeView() {
        setToolbar("My Workout Plans", true);
        this.recyclerViewPlan = (RecyclerView) findViewById(R.id.recyclerView);
        this.textViewEmpty = (TextView) findViewById(R.id.textViewEmpty);
        this.recyclerViewPlan.setLayoutManager(new LinearLayoutManager(this));
        this.recyclerViewPlan.addItemDecoration(new SimpleDividerItemDecoration(this));
        this.planRecyclerViewAdapter = new PlanRecyclerViewAdapter(this, this.planList, this);
        this.recyclerViewPlan.setAdapter(this.planRecyclerViewAdapter);
        ((FloatingActionButton) findViewById(R.id.floatingActionButton)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MyPlanListActivity.this.startActivity(new Intent(MyPlanListActivity.this, CreateWorkoutPlanActivity.class));
            }
        });
    }

    public void onResume() {
        super.onResume();
        prepareMyPlansList();
    }

    private void prepareMyPlansList() {
        this.planList.clear();
        this.planList.addAll(DatabaseHelper.getInstance(this).getMyPlansWorkoutList());
        this.planRecyclerViewAdapter.notifyDataSetChanged();
        if (this.planList.size() == 0) {
            this.textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            this.textViewEmpty.setVisibility(View.GONE);
        }
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        if (model instanceof Plan) {
            Plan plan = (Plan) model;
            Intent intent = new Intent(this, PlanDetailActivity.class);
            Bundle bundle = new Bundle();
            plan.setMyPlan(true);
            bundle.putParcelable("PLAN_OBJECT", plan);
            intent.putExtras(bundle);
            try {
                Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Opened custom plan").putCustomAttribute("Custom opened plan name", plan.getPlan_name()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case R.id.add:
                startActivity(new Intent(this, CreateWorkoutPlanActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_plan_menu, menu);
        return true;
    }

    public void deletePlan(Plan plan) {
        DatabaseHelper.getInstance(this).deletePlan(plan);
        prepareMyPlansList();
    }
}
