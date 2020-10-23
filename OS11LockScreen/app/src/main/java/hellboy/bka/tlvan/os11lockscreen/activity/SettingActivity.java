package hellboy.bka.tlvan.os11lockscreen.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import hellboy.bka.tlvan.os11lockscreen.R;
import hellboy.bka.tlvan.os11lockscreen.util.Utils;
import hellboy.bka.tlvan.os11lockscreen.util.Values;


public class SettingActivity extends AppCompatActivity implements OnClickListener {
    public static final int KEY_CHANGE_PASS = 1243;
    public static final int KEY_DISABLE_PASS = 1213;
    public static final int KEY_NEW_PASS = 1276;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private CheckBox cbActivate;
    private CheckBox cbPassCode;
    private CheckBox cbSound;
    private CheckBox cbVibrate;
    private Editor editor;
    private RelativeLayout menuActivate;
    private RelativeLayout menuChangePass;
    private RelativeLayout menuDisableLock;
    private RelativeLayout menuPass;
    private RelativeLayout menuRate;
    private RelativeLayout menuReview;
    private RelativeLayout menuShare;
    private RelativeLayout menuSound;
    private RelativeLayout menuTimeFormat;
    private RelativeLayout menuTimeOut;
    private RelativeLayout menuUnlockText;
    private RelativeLayout menuVibrate;
    private RelativeLayout menuWallpaper;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;
    private InterstitialAd mInterstitialAd;
    private com.facebook.ads.InterstitialAd interstitialAd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        showFAd();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = this.sharedPreferences.edit();
        setContentView((int) R.layout.activity_setting);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.menuActivate = (RelativeLayout) findViewById(R.id.menuActivate);
        this.menuActivate.setOnClickListener(this);
        this.menuReview = (RelativeLayout) findViewById(R.id.menuReview);
        this.menuReview.setOnClickListener(this);
        this.menuShare = (RelativeLayout) findViewById(R.id.menuShare);
        this.menuShare.setOnClickListener(this);
        this.menuRate = (RelativeLayout) findViewById(R.id.menuRate);
        this.menuRate.setOnClickListener(this);
        this.menuPass = (RelativeLayout) findViewById(R.id.menuPass);
        this.menuPass.setOnClickListener(this);
        this.menuChangePass = (RelativeLayout) findViewById(R.id.menuChangePass);
        this.menuChangePass.setOnClickListener(this);
        this.menuTimeFormat = (RelativeLayout) findViewById(R.id.menuTimeFormat);
        this.menuTimeFormat.setOnClickListener(this);
        this.menuWallpaper = (RelativeLayout) findViewById(R.id.menuWallpaper);
        this.menuWallpaper.setOnClickListener(this);
        this.menuUnlockText = (RelativeLayout) findViewById(R.id.menuUnlockText);
        this.menuUnlockText.setOnClickListener(this);
        this.menuSound = (RelativeLayout) findViewById(R.id.menuSound);
        this.menuSound.setOnClickListener(this);
        this.menuVibrate = (RelativeLayout) findViewById(R.id.menuVibrate);
        this.menuVibrate.setOnClickListener(this);
        this.menuDisableLock = (RelativeLayout) findViewById(R.id.menuDisableLock);
        this.menuDisableLock.setOnClickListener(this);
        this.menuTimeOut = (RelativeLayout) findViewById(R.id.menuTimeout);
        this.menuTimeOut.setOnClickListener(this);
        this.cbActivate = (CheckBox) findViewById(R.id.cbActivate);
        this.cbPassCode = (CheckBox) findViewById(R.id.cbPassCode);
        this.cbSound = (CheckBox) findViewById(R.id.cbSound);
        this.cbVibrate = (CheckBox) findViewById(R.id.cbVibrate);
        this.cbSound.setChecked(this.sharedPreferences.getBoolean(Values.ENABLE_SOUND, true));
        this.cbVibrate.setChecked(this.sharedPreferences.getBoolean(Values.ENABLE_VIBRATE, true));
        boolean passCode = this.sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, false);
        this.cbPassCode.setChecked(passCode);
        if (passCode) {
            this.menuChangePass.setVisibility(View.VISIBLE);
        } else {
            this.menuChangePass.setVisibility(View.GONE);
        }
        this.cbActivate.setChecked(this.sharedPreferences.getBoolean(Values.ACTIVATE_LOCK, true));
    }

    protected void onResume() {
        super.onResume();
    }

    public void onBackPressed() {

    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);

            }
            if (!checkAndRequestPermissions()) {
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("Request")
                        .setMessage("Permission Required!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SettingActivity.this,
                                        new String[]{
                                                Manifest.permission.READ_PHONE_STATE,
                                                Manifest.permission.READ_CONTACTS,
                                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECEIVE_SMS,
                                                Manifest.permission.ACCESS_NETWORK_STATE
                                        },
                                        REQUEST_ID_MULTIPLE_PERMISSIONS);
                            }
                        })
                        .create()
                        .show();
            }
        }
    }

    private boolean checkAndRequestPermissions() {

        int voice = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int internet = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int wifi = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int vibrate = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int network = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (voice != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (wifi != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (vibrate != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (network != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            return false;
        }
        return true;
    }

    private void changeUnlockText() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.dialog_change_unlock_text);
        final EditText editText = (EditText) dialog.findViewById(R.id.edtUnlock);
        editText.setText(this.sharedPreferences.getString(Values.UNLOCK_TEXT, getString(R.string.slidetounlock)));
        ((Button) dialog.findViewById(R.id.btnOk)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (text.length() > 0) {
                    SettingActivity.this.editor.putString(Values.UNLOCK_TEXT, text);
                    SettingActivity.this.editor.commit();
                    dialog.dismiss();
                }
            }
        });
        ((Button) dialog.findViewById(R.id.btnCancel)).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
    //evans
    void showFAd() {
        interstitialAd = new com.facebook.ads.InterstitialAd(this, "140719806502057_140721159835255");
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e("LEVAN","Error: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Show the ad when it's done loading.
                    interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd();
    }

    private void changeTimeFormat() {
        int pos = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence[] format = new CharSequence[]{getString(R.string.time_format_12), getString(R.string.time_format_24)};
        if (!this.sharedPreferences.getBoolean(Values.TIME_FORMAT, false)) {
            pos = 1;
        }
        builder.setSingleChoiceItems(format, pos, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SettingActivity.this.editor.putBoolean(Values.TIME_FORMAT, which == 0);
                SettingActivity.this.editor.commit();
            }
        });
        builder.show();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menuActivate:
                boolean oldStateActivate = this.cbActivate.isChecked();
                this.cbActivate.setChecked(!oldStateActivate);
                this.editor.putBoolean(Values.ACTIVATE_LOCK, !oldStateActivate);
                this.editor.commit();
                return;
            case R.id.menuDisableLock:
                showFAd();
                Intent intent = new Intent();
                intent.setAction("android.app.action.SET_NEW_PASSWORD");
                startActivity(intent);
                return;
            case R.id.menuWallpaper:
                showFAd();
                changeWallpaper();
                return;

            case R.id.menuReview:

                if (this.sharedPreferences.getBoolean(Values.ENABLE_PASSCODE, false)) {
                    startActivity(new Intent(this, LockHasPasscode.class));
                    return;
                } else {
                    startActivity(new Intent(this, LockNoPasscode.class));
                    return;
                }
            case R.id.menuTimeout:
                CharSequence[] time_out = getResources().getStringArray(R.array.time_out);
                int i = 7;
                final long[] timeLong = new long[]{0, 5000, 10000, 20000, 30000, 60000, 120000};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                int which = 1;
                long time = this.sharedPreferences.getLong(Values.TIME_OUT, 10000);
                for (int i2 = 0; i2 < timeLong.length; i2++) {
                    if (timeLong[i2] == time) {
                        which = i2;
                        builder.setSingleChoiceItems(time_out, which, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SettingActivity.this.editor.putLong(Values.TIME_OUT, timeLong[which]);
                                SettingActivity.this.editor.commit();
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        return;
                    }
                }
//                builder.setSingleChoiceItems(time_out, which, (DialogInterface.OnClickListener) /* anonymous class already generated */);
                builder.show();
                return;
            case R.id.menuPass:
                showFAd();
                if (this.cbPassCode.isChecked()) {
                    startActivityForResult(new Intent(this, DisablePass.class), KEY_DISABLE_PASS);
                    return;
                } else {
                    startActivityForResult(new Intent(this, ChangePassCodeActivity.class), KEY_CHANGE_PASS);
                    return;
                }
            case R.id.menuChangePass:
                startActivityForResult(new Intent(this, DisablePass.class), KEY_NEW_PASS);
                return;
            case R.id.menuTimeFormat:
                changeTimeFormat();
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_setting));

                AdRequest adRequest = new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19")
                        .build();
                Log.e("Levan_TIME","here");
                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest);

                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                        Log.e("ERROR_admod", "Ad failed to load! error code: " + errorCode);
                    }
                    public void onAdClosed() {
                        // Load the next interstitial.
                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
                    }
                });
                return;
            case R.id.menuUnlockText:
                changeUnlockText();
                mInterstitialAd = new InterstitialAd(this);
                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_change_text));

                AdRequest adRequest1 = new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19")
                        .build();
                Log.e("Levan_Text","here");
                // Load ads into Interstitial Ads
                mInterstitialAd.loadAd(adRequest1);

                mInterstitialAd.setAdListener(new AdListener() {
                    public void onAdLoaded() {
                        showInterstitial();
                    }
                    public void onAdFailedToLoad(int errorCode) {
                        // Code to be executed when an ad request fails.
                        Log.e("ERROR_admod","Ad failed to load! error code: " + errorCode);
                    }
                    public void onAdClosed() {
                        // Load the next interstitial.
                        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("3F90C25D51474DDEA1C2B2319E1ADE19").build());
                    }
                });
                return;
            case R.id.menuSound:
                boolean sound = this.sharedPreferences.getBoolean(Values.ENABLE_SOUND, true);
                this.cbSound.setChecked(!sound);
                this.editor.putBoolean(Values.ENABLE_SOUND, !sound);
                this.editor.commit();
                return;
            case R.id.menuVibrate:
                boolean vibrate = this.sharedPreferences.getBoolean(Values.ENABLE_VIBRATE, true);
                this.cbVibrate.setChecked(!vibrate);
                this.editor.putBoolean(Values.ENABLE_VIBRATE, !vibrate);
                this.editor.commit();
                return;
            case R.id.menuShare:
                Utils.shareClick(this, getPackageName());
                //Utils.shareClick(this, "hellboy.bka.tlvan.rocketwarrior");
                return;
            case R.id.menuRate:
                Utils.gotoMarket(this);
                return;
            default:
                return;
        }
    }

    private void changeWallpaper() {
        startActivity(new Intent(this, ChangeBackgroundActivity.class));
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_CHANGE_PASS) {
            if (resultCode == -1) {
                this.editor.putString(Values.PASSCODE, data.getStringExtra("result"));
                this.editor.commit();
                this.menuChangePass.setVisibility(View.VISIBLE);
                this.cbPassCode.setChecked(true);
                this.editor.putBoolean(Values.ENABLE_PASSCODE, true);
                this.editor.commit();
            }
        } else if (requestCode == KEY_DISABLE_PASS) {
            if (resultCode == -1) {
                this.menuChangePass.setVisibility(View.GONE);
                this.cbPassCode.setChecked(false);
                this.editor.putBoolean(Values.ENABLE_PASSCODE, false);
                this.editor.commit();
            }
        } else if (requestCode == KEY_NEW_PASS && resultCode == -1) {
            startActivityForResult(new Intent(this, ChangePassCodeActivity.class), KEY_CHANGE_PASS);
        }
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();

            } else {
                Log.e("permison android M", "Not Allow");
            }
        }
    }
}
