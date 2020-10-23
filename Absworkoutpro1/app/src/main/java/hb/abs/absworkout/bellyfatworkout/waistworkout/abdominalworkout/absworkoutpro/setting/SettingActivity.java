package hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.setting;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;

import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.BuildConfig;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.R;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.base.BackBaseActivity;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.ResourceService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.RestartAppModel;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.SharedPrefsService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.TypeFaceService;
import hb.abs.absworkout.bellyfatworkout.waistworkout.abdominalworkout.absworkoutpro.utils.WeightMetricDialog;
import biz.kasual.materialnumberpicker.MaterialNumberPicker.Builder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BackBaseActivity implements ListCallbackSingleChoice {
    @BindView(R.id.contentSetting)
    RelativeLayout contentSetting;
    @BindView(R.id.genderLayout)
    LinearLayout genderLayout;
    private int genderPosition;
    private Typeface rBold;
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
    @BindView(R.id.sGender)
    AppCompatTextView sGender;
    @BindView(R.id.sGenderImage)
    AppCompatImageView sGenderImage;
    @BindView(R.id.sReadyTime)
    AppCompatTextView sReadyTime;
    @BindView(R.id.sRestTime)
    AppCompatTextView sRestTime;
    @BindView(R.id.sSound)
    AppCompatTextView sSound;
    @BindView(R.id.sWeekStart)
    AppCompatTextView sWeekStart;
    @BindView(R.id.sWeight)
    AppCompatTextView sWeight;
    @BindView(R.id.sWeightMetricText)
    AppCompatTextView sWeightMetricText;
    @BindView(R.id.settingContent)
    LinearLayout settingContent;
    @BindView(R.id.settingToolbarText)
    AppCompatTextView settingToolbarText;
    @BindView(R.id.settingVersionName)
    AppCompatTextView settingVersionName;
    @BindView(R.id.soundLayout)
    LinearLayout soundLayout;
    @BindView(R.id.soundSwitch)
    SwitchCompat soundSwitch;
    @BindView(R.id.sstPersonal)
    AppCompatTextView sstPersonal;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.weekStart)
    AppCompatTextView weekStart;
    @BindView(R.id.weekStartLayout)
    LinearLayout weekStartLayout;
    String[] weekStartList;
    private int weekStartPosition;
    @BindView(R.id.weightLayout)
    FrameLayout weightLayout;
    @BindView(R.id.imgGender)
    ImageView Imggender;

    protected void onCreate(Bundle savedInstanceState) {
        CharSequence string;
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_settings);
        ButterKnife.bind((Activity) this);
        setToolbar();
        initFonts();
        String versionName = BuildConfig.VERSION_NAME;
        this.genderPosition = SharedPrefsService.getInstance().getGender(this) - 1;
        this.weekStartPosition = SharedPrefsService.getInstance().getWeekStart(this);
        this.restDuration = SharedPrefsService.getInstance().getRestTime(this);
        this.restTime.setText(this.restDuration + getResources().getString(R.string.seconds_short));
        this.readyDuration = SharedPrefsService.getInstance().getReadyTime(this);
        this.readyTime.setText(this.readyDuration + getResources().getString(R.string.seconds_short));
        this.weekStartList = getResources().getStringArray(R.array.week_start);
        this.weekStart.setText(this.weekStartList[this.weekStartPosition]);
        AppCompatTextView appCompatTextView = this.sWeightMetricText;
        if (SharedPrefsService.getInstance().getWeightMetric(this) != 1) {
            string = getString(R.string.kg);
        } else {
            string = getString(R.string.lb);
        }
        appCompatTextView.setText(string);
        this.settingVersionName.setText("v" + versionName);
        this.contentSetting.setVisibility(0);
        this.genderLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SettingActivity.this.genderDialog();
            }
        });
        this.weightLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                WeightMetricDialog.getInstance().show(SettingActivity.this, SettingActivity.this);
            }
        });
        this.soundSwitch.setChecked(SharedPrefsService.getInstance().getSoundStatus(this));
        this.soundSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
        switch (this.genderPosition) {
            case 0:
                Glide.with((FragmentActivity) this).load(Integer.valueOf(ResourceService.getInstance().getdrawableResourceId("boy", this))).into(this.sGenderImage);
                Glide.with((FragmentActivity) this).load(Integer.valueOf(ResourceService.getInstance().getdrawableResourceId("boy", this))).into(this.Imggender);
                return;
            case 1:
                Glide.with((FragmentActivity) this).load(Integer.valueOf(ResourceService.getInstance().getdrawableResourceId("girl", this))).into(this.sGenderImage);
                Glide.with((FragmentActivity) this).load(Integer.valueOf(ResourceService.getInstance().getdrawableResourceId("girl", this))).into(this.Imggender);
                return;
            default:
                return;
        }
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
        this.rBold = TypeFaceService.getInstance().getRobotoBold(this);
        setFonts();
    }

    private void setFonts() {
        this.sWeight.setTypeface(this.rRegular);
        this.sWeightMetricText.setTypeface(this.rRegular);
        this.sGender.setTypeface(this.rRegular);
        this.weekStart.setTypeface(this.rRegular);
        this.sWeekStart.setTypeface(this.rRegular);
        this.sSound.setTypeface(this.rRegular);
        this.sstPersonal.setTypeface(this.rRegular);
        this.restTime.setTypeface(this.rRegular);
        this.sRestTime.setTypeface(this.rRegular);
        this.settingVersionName.setTypeface(this.rRegular);
        this.settingToolbarText.setTypeface(this.rLight);
    }

    private void genderDialog() {
        Collection langList = new ArrayList();
        langList.add(getString(R.string.male).toUpperCase());
        langList.add(getString(R.string.female).toUpperCase());
        new MaterialDialog.Builder(this).title((int) R.string.setting_ch_gender).items(langList).itemsCallbackSingleChoice(this.genderPosition, new ListCallbackSingleChoice() {
            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                SharedPrefsService.getInstance().setGender(SettingActivity.this, which + 1);
                RestartAppModel.getInstance().settingsChanged(true);
                SettingActivity.this.finish();
                return true;
            }
        }).positiveText((int) R.string.setting_ok).negativeText((int) R.string.setting_cancel).show();
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

    private void restTimePicker(final boolean isRest) {
        final int defaltDuration = isRest ? this.restDuration : this.readyDuration;
        runOnUiThread(new Runnable() {
            public void run() {
                final NumberPicker numberPicker = new Builder(SettingActivity.this.getApplicationContext()).minValue(3).maxValue(60).defaultValue(defaltDuration).backgroundColor(-1).separatorColor(0).textColor(ViewCompat.MEASURED_STATE_MASK).textSize(20.0f).enableFocusability(false).wrapSelectorWheel(true).build();
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

    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        this.sWeightMetricText.setText(which != 1 ? getString(R.string.kg) : getString(R.string.lb));
        SharedPrefsService.getInstance().setWeightMetric(this, which);
        RestartAppModel.getInstance().settingsChanged(true);
        return true;
    }
}
