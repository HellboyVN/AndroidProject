package lp.me.lockos10.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;

import lp.me.lockos10.R;
import lp.me.lockos10.util.Utils;
import lp.me.lockos10.util.Values;

public class SettingActivity extends AppCompatActivity implements OnClickListener {
    public static final int KEY_CHANGE_PASS = 1243;
    public static final int KEY_DISABLE_PASS = 1213;
    public static final int KEY_NEW_PASS = 1276;
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
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 5469;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
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
        }
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
                Intent intent = new Intent();
                intent.setAction("android.app.action.SET_NEW_PASSWORD");
                startActivity(intent);
                return;
            case R.id.menuWallpaper:
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
                return;
            case R.id.menuUnlockText:
                changeUnlockText();
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
