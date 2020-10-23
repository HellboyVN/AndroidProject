package com.workout.workout.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.workout.workout.R;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.modal.Plan;
import com.workout.workout.util.AppUtil;

import java.util.List;

public class PlanDescriptionActivity extends BaseActivity implements PurchasesUpdatedListener {
    Activity activity;
    private Button buttonGetPlan;
    private ImageView imageViewPlan;
    Intent intent;
    private BillingClient mBillingClient;
    private Plan plan;
    String plan_type;
    private ProgressBar progressBar;
    private TextView textViewPlanDescription;
    private TextView textViewPlanName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_plan_description);
        this.activity = this;
        getData();
        initializeViews();
        preparebillingClient();
        try {
            Answers.getInstance().logContentView((ContentViewEvent) ((ContentViewEvent) new ContentViewEvent().putContentName("Plan Description Activity Open").putCustomAttribute("Which plans's Description", this.plan.getPlan_name())).putCustomAttribute("Which plans's sku Description", this.plan.getPlan_id()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getData() {
        this.plan = (Plan) getIntent().getParcelableExtra("PLAN_OBJECT");
        this.intent = new Intent(this, PlanDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("PLAN_OBJECT", this.plan);
        this.intent.putExtras(bundle);
    }

    private void preparebillingClient() {
        this.mBillingClient =  BillingClient.newBuilder(this).setListener(this).build();
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int billingResponseCode) {
                if (billingResponseCode != 0) {
                }
            }

            public void onBillingServiceDisconnected() {
            }
        });
    }

    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
        if (responseCode == 0 && purchases != null) {
            for (Purchase purchase : purchases) {
                Toast.makeText(this, "Successfully purchased", Toast.LENGTH_LONG).show();
                PersistenceManager.unlockThePlan(purchase.getSku());
                if (PersistenceManager.isPlanLocked(this.plan.getPlan_id())) {
                    this.buttonGetPlan.setText(this.buttonGetPlan.getText().toString());
                } else {
                    this.buttonGetPlan.setText("Open");
                }
            }
        } else if (responseCode != 1 && responseCode != -1 && responseCode != 5 && responseCode != 3 && responseCode != 6 && responseCode != -2 && responseCode != 7 && responseCode != 2 && responseCode == 4) {
        }
    }

    private void initializeViews() {
        this.buttonGetPlan = (Button) findViewById(R.id.buttonGetPlan);
        this.textViewPlanDescription = (TextView) findViewById(R.id.textViewPlanDescription);
        this.textViewPlanName = (TextView) findViewById(R.id.textViewPlanName);
        this.imageViewPlan = (ImageView) findViewById(R.id.imageViewPlan);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (this.plan != null) {
            final String planName = this.plan.getPlan_name().replace("\n", "<br>");
            String planDescription = this.plan.getPlan_description();
            String planId = this.plan.getPlan_id();
            this.plan_type = this.plan.getPlan_type();
            String planImageUrl = this.plan.getPlan_image_url();
            if (this.plan_type.equalsIgnoreCase("free") || !PersistenceManager.isPlanLocked(planId)) {
                this.buttonGetPlan.setText("Open");
            } else {
                this.buttonGetPlan.setText("Get this plan");
            }
            this.buttonGetPlan.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (PlanDescriptionActivity.this.plan_type.equalsIgnoreCase("free") || !PersistenceManager.isPlanLocked(PlanDescriptionActivity.this.plan.getPlan_id())) {
                        PlanDescriptionActivity.this.startActivity(PlanDescriptionActivity.this.intent);
                        PlanDescriptionActivity.this.finish();
                        return;
                    }
                    PlanDescriptionActivity.this.mBillingClient.launchBillingFlow(PlanDescriptionActivity.this.activity,  BillingFlowParams.newBuilder().setSku(PlanDescriptionActivity.this.plan.getPlan_id()).setType(PlanDescriptionActivity.this.plan.getPlan_type()).build());
                    try {
                        Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("Get this plan clicked").putCustomAttribute("Get this plan name", planName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            this.textViewPlanName.setText(Html.fromHtml(planName));
            this.textViewPlanDescription.setText(Html.fromHtml(planDescription.replace("\n", "<br>")));
            if (AppUtil.isEmpty(planImageUrl)) {
                this.progressBar.setVisibility(View.VISIBLE);
                int resID = getResources().getIdentifier(this.plan.getPlan_image_name(), "drawable", getPackageName());
                if (resID != 0) {
                    Glide.with((FragmentActivity) this).load(Integer.valueOf(resID)).crossFade().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<Integer, GlideDrawable>() {
                        public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                            PlanDescriptionActivity.this.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            PlanDescriptionActivity.this.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(this.imageViewPlan);
                    return;
                } else {
                    this.progressBar.setVisibility(View.GONE);
                    return;
                }
            }
            this.progressBar.setVisibility(View.VISIBLE);
            Glide.with((FragmentActivity) this).load(planImageUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).listener(new RequestListener<String, GlideDrawable>() {
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    PlanDescriptionActivity.this.progressBar.setVisibility(View.GONE);
                    return false;
                }

                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    PlanDescriptionActivity.this.progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(this.imageViewPlan);
        }
    }
}
