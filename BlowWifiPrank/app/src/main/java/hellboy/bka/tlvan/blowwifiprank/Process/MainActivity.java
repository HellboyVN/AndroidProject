package hellboy.bka.tlvan.blowwifiprank.Process;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import hellboy.bka.tlvan.blowwifiprank.R;

/**
 * Created by tlvan on 9/16/2017.
 */

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    Button btn_blow,btn_shake,btn_result;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        //adv
        MobileAds.initialize(this, " ca-app-pub-6893701289996917~5607106329");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        //adv
        btn_blow = (Button)findViewById(R.id.btn_blow);
        btn_shake = (Button)findViewById(R.id.btn_shake);
        btn_result = (Button)findViewById(R.id.btn_result);
        btn_blow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    Intent intent = new Intent(MainActivity.this, ActivityBlow.class);
                    startActivity(intent);

                } else {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Warning")
                            .setMessage("You need Access all required Permissions")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Prompt the user once explanation has been shown
                                    ActivityCompat.requestPermissions(MainActivity.this,
                                            new String[]{
                                                    Manifest.permission.RECORD_AUDIO,
                                                    Manifest.permission.ACCESS_WIFI_STATE,
                                                    Manifest.permission.VIBRATE,Manifest.permission.INTERNET,
                                                    Manifest.permission.ACCESS_NETWORK_STATE
                                            },
                                            REQUEST_ID_MULTIPLE_PERMISSIONS);
                                }
                            })
                            .create()
                            .show();
                }


            }
        });
        btn_shake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityShaking.class);
                startActivity(intent);
            }
        });
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivitySumary.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    private boolean checkAndRequestPermissions() {

        int voice = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int wifi = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        int vibrate = ContextCompat.checkSelfPermission(this, android.Manifest.permission.VIBRATE);
        int network = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (voice != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (wifi != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.VIBRATE);
        }
        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

}
