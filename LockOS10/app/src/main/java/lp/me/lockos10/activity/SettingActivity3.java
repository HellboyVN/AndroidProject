package lp.me.lockos10.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import lp.me.lockos10.R;

public class SettingActivity3 extends AppCompatActivity implements View.OnClickListener{
    public static final int KEY_CHANGE_PASS = 1243;
    public static final int KEY_DISABLE_PASS = 1213;
    public static final int KEY_NEW_PASS = 1276;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private SwitchCompat swActivate;
    private SwitchCompat swPassCode;
    private SwitchCompat swSound;
    private SwitchCompat swVibrate;

    private RelativeLayout menuActive;

    private RelativeLayout menuLock;
    private RelativeLayout menuWallpaper;
    private RelativeLayout menuTimeout;
    private RelativeLayout menuPreview;

    private RelativeLayout menuPasscode;

    private RelativeLayout menuSound;
    private RelativeLayout menuVibrate;
    private RelativeLayout menuTimeFormat;
    private RelativeLayout menuUnlockText;

    private RelativeLayout menuRate;
    private RelativeLayout menuMore;
    private RelativeLayout menuShare;
    private RelativeLayout menuUninstall;
    private RelativeLayout menuPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting3);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = this.sharedPreferences.edit();

        bindUI();
    }

    void bindUI(){
        //menu
        menuActive = (RelativeLayout) findViewById(R.id.menu_activate);

        menuLock = (RelativeLayout) findViewById(R.id.menu_lockscreen);
        menuWallpaper = (RelativeLayout) findViewById(R.id.menu_wallpaper);
        menuTimeout = (RelativeLayout) findViewById(R.id.menu_timeout);
        menuPreview = (RelativeLayout) findViewById(R.id.menu_preview);

        menuPasscode = (RelativeLayout) findViewById(R.id.menu_security);

        menuSound = (RelativeLayout) findViewById(R.id.menu_sound);
        menuVibrate = (RelativeLayout) findViewById(R.id.menu_vibrate);
        menuTimeFormat = (RelativeLayout) findViewById(R.id.menu_timeformat);
        menuUnlockText = (RelativeLayout) findViewById(R.id.menu_unlocktext);

        menuRate = (RelativeLayout) findViewById(R.id.menu_rate);
        menuMore = (RelativeLayout) findViewById(R.id.menu_more);
        menuShare = (RelativeLayout) findViewById(R.id.menu_share);
        menuUninstall = (RelativeLayout) findViewById(R.id.menu_uninstall);
        menuPolicy = (RelativeLayout) findViewById(R.id.menu_policy);

        menuActive.setOnClickListener(this);

        menuLock.setOnClickListener(this);
        menuWallpaper.setOnClickListener(this);
        menuTimeout.setOnClickListener(this);
        menuPreview.setOnClickListener(this);

        menuPasscode.setOnClickListener(this);

        menuSound.setOnClickListener(this);
        menuVibrate.setOnClickListener(this);
        menuTimeFormat.setOnClickListener(this);
        menuUnlockText.setOnClickListener(this);

        menuRate.setOnClickListener(this);
        menuMore.setOnClickListener(this);
        menuShare.setOnClickListener(this);
        menuUninstall.setOnClickListener(this);
        menuPolicy.setOnClickListener(this);

        //switch
        swActivate = (SwitchCompat) findViewById(R.id.sw_enable);
        swPassCode = (SwitchCompat) findViewById(R.id.sw_security);
        swSound = (SwitchCompat) findViewById(R.id.sw_sound);
        swVibrate = (SwitchCompat) findViewById(R.id.sw_vibrate);
        //load pref

    }


    @Override
    public void onClick(View view) {



    }
}
