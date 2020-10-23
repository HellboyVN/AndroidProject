package com.workout.workout.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.database.DatabaseError;
import java.lang.Thread.UncaughtExceptionHandler;

public class AppRate implements OnClickListener, OnCancelListener {
    private static final String TAG = "AppRater";
    private OnClickListener clickListener;
    private Builder dialogBuilder = null;
    private Activity hostActivity;
    private long minDaysUntilPrompt = 0;
    private long minLaunchesUntilPrompt = 0;
    private SharedPreferences preferences;
    private boolean showIfHasCrashed = true;

    public AppRate(Activity hostActivity) {
        this.hostActivity = hostActivity;
        this.preferences = hostActivity.getSharedPreferences(PrefsContract.SHARED_PREFS_NAME, 0);
    }

    public AppRate setMinLaunchesUntilPrompt(long minLaunchesUntilPrompt) {
        this.minLaunchesUntilPrompt = minLaunchesUntilPrompt;
        return this;
    }

    public AppRate setMinDaysUntilPrompt(long minDaysUntilPrompt) {
        this.minDaysUntilPrompt = minDaysUntilPrompt;
        return this;
    }

    public AppRate setShowIfAppHasCrashed(boolean showIfCrash) {
        this.showIfHasCrashed = showIfCrash;
        return this;
    }

    public AppRate setCustomDialog(Builder customBuilder) {
        this.dialogBuilder = customBuilder;
        return this;
    }

    public static void reset(Context context) {
        context.getSharedPreferences(PrefsContract.SHARED_PREFS_NAME, 0).edit().clear().commit();
        Log.d(TAG, "Cleared AppRate shared preferences.");
    }

    public void init() {
        Log.d(TAG, "Init AppRate");
        if (!this.preferences.getBoolean(PrefsContract.PREF_DONT_SHOW_AGAIN, false)) {
            if (!this.preferences.getBoolean(PrefsContract.PREF_APP_HAS_CRASHED, false) || this.showIfHasCrashed) {
                if (!this.showIfHasCrashed) {
                    initExceptionHandler();
                }
                Editor editor = this.preferences.edit();
                long launch_count = this.preferences.getLong(PrefsContract.PREF_LAUNCH_COUNT, 0) + 1;
                editor.putLong(PrefsContract.PREF_LAUNCH_COUNT, launch_count);
                if (Long.valueOf(this.preferences.getLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, 0)).longValue() == 0) {
                    editor.putLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, Long.valueOf(System.currentTimeMillis()).longValue());
                }
                if (launch_count >= this.minLaunchesUntilPrompt) {
                    if (this.dialogBuilder != null) {
                        showDialog(this.dialogBuilder);
                    } else {
                        showDefaultDialog();
                    }
                }
                editor.commit();
            }
        }
    }

    private void initExceptionHandler() {
        Log.d(TAG, "Init AppRate ExceptionHandler");
        UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(currentHandler instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(currentHandler, this.hostActivity));
        }
    }

    private void showDefaultDialog() {
        Log.d(TAG, "Create default dialog.");
        String title = "Rate " + getApplicationName(this.hostActivity.getApplicationContext());
        new Builder(this.hostActivity).setTitle(title).setMessage("If you enjoy using " + getApplicationName(this.hostActivity.getApplicationContext()) + ", please take a moment to rate it. Thanks for your support!").setPositiveButton("Rate it !", this).setNegativeButton("No thanks", this).setNeutralButton("Later", this).setOnCancelListener(this).create().show();
    }

    private void showDialog(Builder builder) {
        Log.d(TAG, "Create custom dialog.");
        AlertDialog dialog = builder.create();
        dialog.show();
        String remindLater = (String) dialog.getButton(-3).getText();
        String dismiss = (String) dialog.getButton(-2).getText();
        dialog.setButton(-1, (String) dialog.getButton(-1).getText(), this);
        dialog.setButton(-3, remindLater, this);
        dialog.setButton(-2, dismiss, this);
        dialog.setOnCancelListener(this);
    }

    public void onCancel(DialogInterface dialog) {
        Editor editor = this.preferences.edit();
        editor.putLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, System.currentTimeMillis());
        editor.putLong(PrefsContract.PREF_LAUNCH_COUNT, 0);
        editor.commit();
    }

    public AppRate setOnClickListener(OnClickListener onClickListener) {
        this.clickListener = onClickListener;
        return this;
    }

    public void onClick(DialogInterface dialog, int which) {
        Editor editor = this.preferences.edit();
        switch (which) {
            case DatabaseError.PERMISSION_DENIED /*-3*/:
                editor.putLong(PrefsContract.PREF_DATE_FIRST_LAUNCH, System.currentTimeMillis());
                editor.putLong(PrefsContract.PREF_LAUNCH_COUNT, 0);
                break;
            case -2:
                editor.putBoolean(PrefsContract.PREF_DONT_SHOW_AGAIN, true);
                break;
            case -1:
                try {
                    this.hostActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.hostActivity.getPackageName())));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this.hostActivity, "No Play Store installed on device", Toast.LENGTH_SHORT).show();
                }
                editor.putBoolean(PrefsContract.PREF_DONT_SHOW_AGAIN, true);
                break;
        }
        editor.commit();
        dialog.dismiss();
        if (this.clickListener != null) {
            this.clickListener.onClick(dialog, which);
        }
    }

    private static final String getApplicationName(Context context) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "(unknown)");
    }
}
