package com.workout.workout.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.Builder;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics.Event;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.workout.workout.R;
import com.workout.workout.activity.MyPlanListActivity;
import com.workout.workout.activity.PlanDetailActivity;
import com.workout.workout.activity.PremiumVersion;
import com.workout.workout.adapter.SampleAdapter;
import com.workout.workout.adapter.StickyHeaderGridLayoutManager;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.database.DatabaseHelper;
import com.workout.workout.listener.OnListFragmentInteractionListener;
import com.workout.workout.managers.PersistenceManager;
import com.workout.workout.modal.BaseModel;
import com.workout.workout.modal.Plan;
import com.workout.workout.modal.PlanCategory;
import com.workout.workout.util.HideShowScrollListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.billingclient.api.BillingClient.newBuilder;

public class PlanFragment extends BaseFragment implements PurchasesUpdatedListener, OnListFragmentInteractionListener, OnClickListener {
    private static final int SPAN_SIZE = 2;
    private FloatingActionButton fab;
    private HashMap<String, ArrayList<Plan>> hashMap = new HashMap();
    private BillingClient mBillingClient;
    private FirebaseAnalytics mFirebaseAnalytics;
    private StickyHeaderGridLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewPlan;
    SampleAdapter sampleAdapter;

    class GetPlanListAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<PlanCategory> planCategoryList;

        GetPlanListAsyncTask() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PlanFragment.this.progressBar.setVisibility(View.VISIBLE);
        }

        protected Void doInBackground(Void... voids) {
            int i;
            this.planCategoryList = DatabaseHelper.getInstance(PlanFragment.this.getContext()).getPlanCategoryList();
            for (i = 0; i < this.planCategoryList.size(); i++) {
                ArrayList<Plan> plansArrayList = DatabaseHelper.getInstance(PlanFragment.this.getContext()).getPlansList(((PlanCategory) this.planCategoryList.get(i)).getPlan_category_id());
                int j = 0;
                while (j < plansArrayList.size()) {
                    if (((Plan) plansArrayList.get(j)).getPlan_type() != null && ((Plan) plansArrayList.get(j)).getPlan_type().equals("free")) {
                        ((Plan) plansArrayList.get(j)).setLocked(false);
                    } else if (PersistenceManager.isPlanLocked(((Plan) plansArrayList.get(j)).getPlan_id())) {
                        ((Plan) plansArrayList.get(j)).setLocked(true);
                    } else {
                        ((Plan) plansArrayList.get(j)).setLocked(false);
                    }
                    j++;
                }
                PlanFragment.this.hashMap.put(((PlanCategory) this.planCategoryList.get(i)).getPlan_category_id(), plansArrayList);
            }
            for (i = 0; i < this.planCategoryList.size(); i++) {
                ((PlanCategory) this.planCategoryList.get(i)).setPlansList((List) PlanFragment.this.hashMap.get(((PlanCategory) this.planCategoryList.get(i)).getPlan_category_id()));
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PlanFragment.this.progressBar.setVisibility(View.GONE);
            PlanFragment.this.sampleAdapter = new SampleAdapter(PlanFragment.this.getActivity(), this.planCategoryList, PlanFragment.this);
            PlanFragment.this.recyclerViewPlan.setAdapter(PlanFragment.this.sampleAdapter);
        }
    }

    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            preparebillingClient();
        } else {
            preparebillingClient();
        }
    }

    private void preparebillingClient() {
        this.mBillingClient = BillingClient.newBuilder(getActivity()).setListener(this).build();
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int billingResponseCode) {
                if (billingResponseCode != 0) {
                }
            }

            public void onBillingServiceDisconnected() {
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        setHasOptionsMenu(true);
        if (FirebaseRemoteConfig.getInstance().getBoolean("new_plan_banner_ads_enable")) {
            loadBannerAdvertisement(view, AppConstants.ADMOB_PLAN_BANNER_AD_ID);
        }
        initializeView(view);
        return view;
    }

    private void initializeView(View view) {
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        this.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        this.recyclerViewPlan = (RecyclerView) view.findViewById(R.id.recyclerViewPlan);
        this.mLayoutManager = new StickyHeaderGridLayoutManager(2);
        this.mLayoutManager.setHeaderBottomOverlapMargin(getResources().getDimensionPixelSize(R.dimen.header_shadow_size));
        this.recyclerViewPlan.setItemAnimator(new DefaultItemAnimator() {
            public boolean animateRemove(ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        this.recyclerViewPlan.setLayoutManager(this.mLayoutManager);
        this.recyclerViewPlan.setHasFixedSize(true);
        this.fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        this.fab.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Custom plan Floating button click"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                PlanFragment.this.startActivity(new Intent(PlanFragment.this.getActivity(), MyPlanListActivity.class));
            }
        });
        this.recyclerViewPlan.addOnScrollListener(new HideShowScrollListener() {
            public void onHide() {
                PlanFragment.this.fab.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(0.0f).scaleY(0.0f);
            }

            public void onShow() {
                PlanFragment.this.fab.animate().setInterpolator(new AccelerateDecelerateInterpolator()).scaleX(1.0f).scaleY(1.0f);
            }
        });
    }

    public void onResume() {
        super.onResume();
        new GetPlanListAsyncTask().execute(new Void[0]);
    }

    private void preparePlansList() {
        int i;
        ArrayList<PlanCategory> planCategoryList = DatabaseHelper.getInstance(getContext()).getPlanCategoryList();
        for (i = 0; i < planCategoryList.size(); i++) {
            ArrayList<Plan> plansArrayList = DatabaseHelper.getInstance(getContext()).getPlansList(((PlanCategory) planCategoryList.get(i)).getPlan_category_id());
            int j = 0;
            while (j < plansArrayList.size()) {
                if (((Plan) plansArrayList.get(j)).getPlan_type() != null && ((Plan) plansArrayList.get(j)).getPlan_type().equals("free")) {
                    ((Plan) plansArrayList.get(j)).setLocked(false);
                } else if (PersistenceManager.isPremiumVersion() || !PersistenceManager.isPlanLocked(((Plan) plansArrayList.get(j)).getPlan_id())) {
                    ((Plan) plansArrayList.get(j)).setLocked(false);
                } else {
                    ((Plan) plansArrayList.get(j)).setLocked(true);
                }
                j++;
            }
            this.hashMap.put(((PlanCategory) planCategoryList.get(i)).getPlan_category_id(), plansArrayList);
        }
        for (i = 0; i < planCategoryList.size(); i++) {
            ((PlanCategory) planCategoryList.get(i)).setPlansList((List) this.hashMap.get(((PlanCategory) planCategoryList.get(i)).getPlan_category_id()));
        }
        this.sampleAdapter = new SampleAdapter(getActivity(), planCategoryList, this);
        this.recyclerViewPlan.setAdapter(this.sampleAdapter);
    }

    public void onListFragmentInteraction(BaseModel model, int position) {
        if (model instanceof Plan) {
            Plan plan = (Plan) model;
            Intent intent = new Intent(getActivity(), PlanDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("PLAN_OBJECT", plan);
            intent.putExtras(bundle);
            String type_of_purchase = plan.getPlan_type();
            if (plan.getPlan_type().equalsIgnoreCase("free")) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("plan_id", plan.getPlan_id());
                bundle1.putString("plan_name", plan.getPlan_name());
                bundle1.putString(DatabaseHelper.COLUMN_PLAN_TYPE, plan.getPlan_type());
                this.mFirebaseAnalytics.logEvent(Event.VIEW_ITEM, bundle1);
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("free plan Opened").putCustomAttribute("free opened plan name", plan.getPlan_name()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            } else if (!plan.isLocked()) {
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("paid plan Opened").putCustomAttribute("paid opened plan name", plan.getPlan_name()));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                startActivity(intent);
            } else if (!this.mBillingClient.isReady()) {
                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                this.mBillingClient.startConnection(new BillingClientStateListener() {
                    public void onBillingSetupFinished(int billingResponseCode) {
                        if (billingResponseCode != 0) {
                        }
                    }

                    public void onBillingServiceDisconnected() {
                    }
                });
            } else if (PersistenceManager.isPlanLocked(plan.getPlan_id())) {
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) ((ContentViewEvent) new ContentViewEvent().putContentName(plan.getPlan_name()).putContentType("on create view").putContentId(plan.getPlan_id()).putCustomAttribute("Plan price", BigDecimal.valueOf(plan.getPlan_price().longValue()))).putCustomAttribute("response code", Integer.valueOf(this.mBillingClient.launchBillingFlow(getActivity(), BillingFlowParams.newBuilder().setSku(plan.getPlan_id()).setType(type_of_purchase).build()))));
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
            } else {
                plan.setLocked(false);
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("paid plan Opened").putCustomAttribute("paid opened plan name", plan.getPlan_name()));
                } catch (Exception e222) {
                    e222.printStackTrace();
                }
                try {
                    Answers.getInstance().logContentView((ContentViewEvent) new ContentViewEvent().putContentName("paid plan Opened persistence manager").putCustomAttribute("paid opened persistance manager plan name", plan.getPlan_name()));
                } catch (Exception e2222) {
                    e2222.printStackTrace();
                }
                startActivity(intent);
            }
        }
    }

    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
        if (responseCode == 0 && purchases != null) {
            for (Purchase purchase : purchases) {
                Toast.makeText(getActivity(), "Successfully purchased", Toast.LENGTH_LONG).show();
                PersistenceManager.unlockThePlan(purchase.getSku());
                try {
                    Answers.getInstance().logPurchase((PurchaseEvent) ((PurchaseEvent) new PurchaseEvent().putCustomAttribute("Order ID", purchase.getOrderId())).putItemId(purchase.getSku()).putCustomAttribute("response code", Integer.valueOf(responseCode)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (responseCode != 1 && responseCode != -1 && responseCode != 5 && responseCode != 3 && responseCode != 6 && responseCode != -2 && responseCode != 7 && responseCode != 2 && responseCode == 4) {
        }
    }

    public void onClick(View view) {
        int viewId = view.getId();
    }

    private void handleMyWorkouts() {
        startActivity(new Intent(getActivity(), MyPlanListActivity.class));
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.add).setVisible(true);
        if (!PersistenceManager.isPremiumVersion()) {
            menu.findItem(R.id.premium).setVisible(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("Custom plan Action bar button click"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getActivity(), MyPlanListActivity.class));
                return true;
            case R.id.premium:
                try {
                    Answers.getInstance().logContentView(new ContentViewEvent().putContentName("premium button plan action bar click"));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                startActivity(new Intent(getActivity(), PremiumVersion.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
