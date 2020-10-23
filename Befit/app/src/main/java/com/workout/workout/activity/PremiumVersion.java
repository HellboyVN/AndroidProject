package com.workout.workout.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClient.Builder;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.workout.workout.R;
import com.workout.workout.constant.AppConstants;
import com.workout.workout.managers.PersistenceManager;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.android.billingclient.api.BillingClient.newBuilder;

public class PremiumVersion extends BaseActivity implements PurchasesUpdatedListener {
    private Button buttonPremium1;
    private Button buttonPremium2;
    private Button buttonPremium3;
    private BillingClient mBillingClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_premium);
        this.buttonPremium1 = (Button) findViewById(R.id.buttonPremium1);
        this.buttonPremium2 = (Button) findViewById(R.id.buttonPremium2);
        this.buttonPremium3 = (Button) findViewById(R.id.buttonPremium3);
        preparebillingClient();
        this.buttonPremium1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PremiumVersion.this.handlePremium1();
            }
        });
        this.buttonPremium2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PremiumVersion.this.handlePremium2();
            }
        });
        this.buttonPremium3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                PremiumVersion.this.handlePremium3();
            }
        });
    }

    private void preparebillingClient() {
        this.mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        this.mBillingClient.startConnection(new BillingClientStateListener() {
            public void onBillingSetupFinished(int billingResponseCode) {
                if (billingResponseCode != 0) {
                }
            }

            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void handlePremium1() {
        try {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("3 months clicked"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PersistenceManager.isPremiumVersion()) {
            Toast.makeText(this, "Premium Version", Toast.LENGTH_LONG).show();
            return;
        }
        this.mBillingClient.launchBillingFlow(this,  BillingFlowParams.newBuilder().setSku(AppConstants.PREMIUM_VERSION_3_MONTH).setType("subs").build());
    }

    private void handlePremium2() {
        try {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("6 months clicked"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PersistenceManager.isPremiumVersion()) {
            Toast.makeText(this, "Premium Version", Toast.LENGTH_LONG).show();
            return;
        }
        this.mBillingClient.launchBillingFlow(this, BillingFlowParams.newBuilder().setSku(AppConstants.PREMIUM_VERSION_6_MONTH).setType("subs").build());
    }

    private void handlePremium3() {
        try {
            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("lifetime clicked"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PersistenceManager.isPremiumVersion()) {
            Toast.makeText(this, "Premium Version", Toast.LENGTH_LONG).show();
            return;
        }
        this.mBillingClient.launchBillingFlow(this, BillingFlowParams.newBuilder().setSku(AppConstants.SKU_PREMIUM_VERSION).setType("inapp").build());
    }

    public void onPurchasesUpdated(int responseCode, List<Purchase> purchases) {
        if (responseCode == 0 && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getSku().equalsIgnoreCase(AppConstants.SKU_PREMIUM_VERSION)) {
                    Toast.makeText(this, "Premium Version Purchased", Toast.LENGTH_LONG).show();
                    PersistenceManager.setPremiumVersion(true);
                    try {
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (purchase.getSku().equalsIgnoreCase("premiumversion1")) {
                    Toast.makeText(this, "Premium Version Purchased", Toast.LENGTH_LONG).show();
                    PersistenceManager.setPremiumVersion(true);
                    try {
                        finish();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                if (purchase.getSku().equalsIgnoreCase("premiumversion2")) {
                    Toast.makeText(this, "Premium Version Purchased", Toast.LENGTH_LONG).show();
                    PersistenceManager.setPremiumVersion(true);
                    try {
                        finish();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                }
            }
        } else if (responseCode != 1 && responseCode != -1 && responseCode != 5 && responseCode != 3 && responseCode != 6 && responseCode != -2 && responseCode != 7 && responseCode != 2 && responseCode == 4) {
        }
    }
}
