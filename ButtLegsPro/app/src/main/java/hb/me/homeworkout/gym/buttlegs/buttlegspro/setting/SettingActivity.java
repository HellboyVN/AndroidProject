package hb.me.homeworkout.gym.buttlegs.buttlegspro.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;

import biz.kasual.materialnumberpicker.MaterialNumberPicker.Builder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.BuildConfig;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.base.BackBaseActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.database.RealmManager;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.EventCenter;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.DialogHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.RestartAppModel;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SharedPrefsService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;

//import com.google.firebase.perf.metrics.AppStartTrace;
@SuppressWarnings("ResourceType")
public class SettingActivity extends BackBaseActivity implements ListCallbackSingleChoice {
    @BindView(R.id.cdVoice)
    AppCompatTextView cdVoice;
    @BindView(R.id.cdVoiceLabel)
    AppCompatTextView cdVoiceLabel;
    @BindView(R.id.cdVoiceLayout)
    FrameLayout cdVoiceLayout;
    private int cdVoiceStart;
    @BindView(R.id.contentSetting)
    RelativeLayout contentSetting;
    private Typeface rLight;
    private Typeface rRegular;
    int readyDuration = 0;
    @BindView(R.id.readyTime)
    AppCompatTextView readyTime;
    @BindView(R.id.readyTimeLayout)
    FrameLayout readyTimeLayout;
    int restDuration = 0;
    @BindView(R.id.restTime)
    AppCompatTextView restTime;
    @BindView(R.id.restTimeLayout)
    FrameLayout restTimeLayout;
    @BindView(R.id.sReadyTime)
    AppCompatTextView sReadyTime;
    @BindView(R.id.sRestTime)
    AppCompatTextView sRestTime;
    @BindView(R.id.sSound)
    TextView sSound;
    @BindView(R.id.sWeekStart)
    AppCompatTextView sWeekStart;
    @BindView(R.id.sWeightMetricText)
    AppCompatTextView sWeightMetricText;
    @BindView(R.id.settingContent)
    LinearLayout settingContent;
    @BindView(R.id.settingToolbarText)
    TextView settingToolbarText;
    @BindView(R.id.settingVersionName)
    TextView settingVersionName;
    @BindView(R.id.soundSwich)
    SwitchCompat soundSwich;
    @BindView(R.id.sstPersonal)
    TextView sstPersonal;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.weekStart)
    AppCompatTextView weekStart;
    String[] weekStartList;
    private int weekStartPosition;
    @BindView(R.id.weightLayout)
    FrameLayout weightLayout;

    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.setting.SettingActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.setting.SettingActivity");
        super.onStart();
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.setting.SettingActivity");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_setting);
        ButterKnife.bind((Activity) this);
        setToolbar();
        initFonts();
        this.settingVersionName.setText("v" + BuildConfig.VERSION_NAME);
        this.contentSetting.setVisibility(0);
        this.weekStartPosition = SharedPrefsService.getInstance().getWeekStart(this);
        this.weekStartList = getResources().getStringArray(R.array.week_start);
        this.weekStart.setText(this.weekStartList[this.weekStartPosition]);
        this.sWeightMetricText.setText(RealmManager.getInstance().getUser().getWeightUnit() != 1 ? getString(R.string.kg) : getString(R.string.lb));
        this.cdVoiceStart = SharedPrefsService.getInstance().getCDVoiceStart(this);
        if (this.cdVoiceStart == 0) {
            this.cdVoice.setText(getString(R.string.label_no_voice));
        } else {
            this.cdVoice.setText(this.cdVoiceStart + getString(R.string.seconds_short));
        }
        this.restDuration = SharedPrefsService.getInstance().getRestTime(this);
        this.restTime.setText(this.restDuration + getResources().getString(R.string.seconds_short));
        this.readyDuration = SharedPrefsService.getInstance().getReadyTime(this);
        this.readyTime.setText(this.readyDuration + getResources().getString(R.string.seconds_short));
        this.soundSwich.setChecked(SharedPrefsService.getInstance().getSoundStatus(this));
        this.soundSwich.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SharedPrefsService.getInstance().setSoundStatus(SettingActivity.this, true);
                } else {
                    SharedPrefsService.getInstance().setSoundStatus(SettingActivity.this, false);
                }
            }
        });
        this.restTimeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingActivity.this.restTimePicker(true);
            }
        });
        this.readyTimeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingActivity.this.restTimePicker(false);
            }
        });
        this.cdVoiceLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingActivity.this.cdVoicePicker();
            }
        });
        this.weightLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DialogHelper.getInstance().showWeightMetricDialog(SettingActivity.this, SettingActivity.this, RealmManager.getInstance().getUser().getWeightUnit());
            }
        });
    }

    private void setToolbar() {
        setSupportActionBar(this.toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initFonts() {
        this.rLight = TypeFaceService.getInstance().getRobotoLight(this);
        this.rRegular = TypeFaceService.getInstance().getRobotoRegular(this);
        setFonts();
    }

    private void setFonts() {
        this.sSound.setTypeface(this.rRegular);
        this.sstPersonal.setTypeface(this.rRegular);
        this.sWeekStart.setTypeface(this.rRegular);
        this.settingVersionName.setTypeface(this.rRegular);
        this.settingToolbarText.setTypeface(this.rLight);
        this.sRestTime.setTypeface(this.rRegular);
        this.restTime.setTypeface(this.rRegular);
        this.sReadyTime.setTypeface(this.rRegular);
        this.cdVoiceLabel.setTypeface(this.rRegular);
        this.cdVoiceLabel.setTypeface(this.rRegular);
        this.sWeightMetricText.setTypeface(this.rRegular);
    }

    private void restTimePicker(final boolean isRest) {
        final int defaltDuration = isRest ? this.restDuration : this.readyDuration;
        runOnUiThread(new Runnable() {
            public void run() {
                final NumberPicker numberPicker = new Builder(SettingActivity.this.getApplicationContext()).minValue(3).maxValue(30).defaultValue(defaltDuration).backgroundColor(-1).separatorColor(0).textColor(ViewCompat.MEASURED_STATE_MASK).textSize(20.0f).enableFocusability(false).wrapSelectorWheel(true).build();
                new AlertDialog.Builder(SettingActivity.this).setTitle((isRest ? SettingActivity.this.getString(R.string.setting_rest_duration) : SettingActivity.this.getString(R.string.setting_ready_duration)) + "(" + SettingActivity.this.getString(R.string.seconds_short) + ")").setView(numberPicker).setPositiveButton(SettingActivity.this.getString(17039370), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (isRest) {
                            SettingActivity.this.restDuration = numberPicker.getValue();
                            SharedPrefsService.getInstance().setRestTime(SettingActivity.this, SettingActivity.this.restDuration);
                            SettingActivity.this.restTime.setText(SettingActivity.this.restDuration + SettingActivity.this.getResources().getString(R.string.seconds_short));
                            return;
                        }
                        SettingActivity.this.readyDuration = numberPicker.getValue();
                        SharedPrefsService.getInstance().setReadyTime(SettingActivity.this, SettingActivity.this.readyDuration);
                        SettingActivity.this.readyTime.setText(SettingActivity.this.readyDuration + SettingActivity.this.getResources().getString(R.string.seconds_short));
                    }
                }).setNegativeButton(SettingActivity.this.getString(17039360), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });
    }

    private void cdVoicePicker() {
        runOnUiThread(new Runnable() {
            public void run() {
                final NumberPicker numberPicker = new Builder(SettingActivity.this.getApplicationContext()).minValue(0).maxValue(10).defaultValue(SettingActivity.this.cdVoiceStart).backgroundColor(-1).separatorColor(0).textColor(ViewCompat.MEASURED_STATE_MASK).textSize(20.0f).enableFocusability(false).wrapSelectorWheel(true).build();
                new AlertDialog.Builder(SettingActivity.this).setTitle(SettingActivity.this.getString(R.string.setting_cd_voice_starts_in)).setView(numberPicker).setPositiveButton(SettingActivity.this.getString(17039370), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SettingActivity.this.cdVoiceStart = numberPicker.getValue();
                        SharedPrefsService.getInstance().setCDVoiceStart(SettingActivity.this, SettingActivity.this.cdVoiceStart);
                        if (SettingActivity.this.cdVoiceStart == 0) {
                            SettingActivity.this.cdVoice.setText(SettingActivity.this.getString(R.string.label_no_voice));
                        } else {
                            SettingActivity.this.cdVoice.setText(SettingActivity.this.cdVoiceStart + SettingActivity.this.getString(R.string.seconds_short));
                        }
                    }
                }).setNegativeButton(SettingActivity.this.getString(17039360), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
            }
        });
    }

    @OnClick({R.id.weekStart, R.id.weekStartLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.weekStartLayout:
                WeekStartDialog();
                return;
            default:
                return;
        }
    }

    private void WeekStartDialog() {
        new MaterialDialog.Builder(this).title((int) R.string.setting_ch_weekstart).items(this.weekStartList).itemsCallbackSingleChoice(this.weekStartPosition, new ListCallbackSingleChoice() {
            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                SharedPrefsService.getInstance().setWeekStart(SettingActivity.this, which);
                RestartAppModel.getInstance().settingsChanged(true);
                SettingActivity.this.weekStart.setText(SettingActivity.this.weekStartList[which]);
                return true;
            }
        }).positiveText((int) R.string.setting_ok).negativeText((int) R.string.setting_cancel).show();
    }

    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        this.sWeightMetricText.setText(which != 1 ? getString(R.string.kg) : getString(R.string.lb));
        RealmManager.getInstance().changeUserWeightUnit(which);
        EventCenter.getInstance().notifyWeightMetricChanged(which);
        return true;
    }
}
