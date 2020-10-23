package hb.homeworkout.homeworkouts.noequipment.fitnesspro.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


import hb.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.homeworkout.homeworkouts.noequipment.fitnesspro.managers.PersistenceManager;

public class PremiumVersion extends BaseActivity   {
    private Button buttonPremium1;
    private Button buttonPremium2;
    private Button buttonPremium3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_premium);
        this.buttonPremium1 = (Button) findViewById(R.id.buttonPremium1);
        this.buttonPremium2 = (Button) findViewById(R.id.buttonPremium2);
        this.buttonPremium3 = (Button) findViewById(R.id.buttonPremium3);
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



    private void handlePremium1() {
//        try {
//            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("3 months clicked"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (PersistenceManager.isPremiumVersion()) {
            Toast.makeText(this, "Premium Version", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void handlePremium2() {
//        try {
//            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("6 months clicked"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (PersistenceManager.isPremiumVersion()) {
            Toast.makeText(this, "Premium Version", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void handlePremium3() {
//        try {
//            Answers.getInstance().logContentView(new ContentViewEvent().putContentName("lifetime clicked"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (PersistenceManager.isPremiumVersion()) {
            Toast.makeText(this, "Premium Version", Toast.LENGTH_LONG).show();
            return;
        }
    }


}
