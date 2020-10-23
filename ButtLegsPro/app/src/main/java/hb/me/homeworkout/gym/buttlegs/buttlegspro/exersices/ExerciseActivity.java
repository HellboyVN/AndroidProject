package hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
//import com.google.firebase.perf.metrics.AppStartTrace;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.StepperIndicator.OnStepClickListener;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.listeners.IRestartLevel;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.finish.FinishActivity;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.home.type.ExerciseType;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IExerciseNavigation;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.Exercise;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.models.LevelData;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ConsKeys;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.ResourceService;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.SoundHelper;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.utils.TypeFaceService;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity implements IExerciseNavigation {
    static final /* synthetic */ boolean $assertionsDisabled = (!ExerciseActivity.class.desiredAssertionStatus());
    List<Exercise> exercices;
    LevelData exerciseData;
    @BindView(R.id.exersiceRepeatition)
    TextView exersiceRepeatition;
    @BindView(R.id.exersiceRound)
    TextView exersiceRound;
    IRestartLevel iRestartLevelListener;
    boolean isFinish = false;
    @BindView(R.id.treadmill_layout)
    LinearLayout layoutTread;
    ExerciseAdapter mAdapter;
    private ExerciseType mExType;
    NonSwipeableViewPager mExercisePager;
    private MoveReason mMoveReason = MoveReason.NATURAL_RIGHT;
    @BindView(R.id.tmSpeed)
    TextView tmSpeed;
    @BindView(R.id.toolbarExersice)
    Toolbar toolbarExersice;
    @BindView(R.id.toolbarExersiceText)
    TextView toolbarExersiceText;

    public enum MoveReason {
        NATURAL_RIGHT,
        MANUAL_LEFT,
        MANUAL_RIGHT
    }

    protected void onResume() {
//        AppStartTrace.setLauncherActivityOnResumeTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseActivity");
        super.onResume();
    }

    protected void onStart() {
//        AppStartTrace.setLauncherActivityOnStartTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseActivity");
        super.onStart();
    }

    public IRestartLevel getiRestartLevelListener() {
        return this.iRestartLevelListener;
    }

    protected void onCreate(Bundle bundle) {
//        AppStartTrace.setLauncherActivityOnCreateTime("hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseActivity");
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_excercise);
        ButterKnife.bind((Activity) this);
        setSupportActionBar(this.toolbarExersice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setVolumeControlStream(3);
        this.exerciseData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.EXERCISE_DATA_KEY);
        this.mExType = ExerciseType.getByType(getIntent().getExtras().getInt(ConsKeys.EXERCISE_DATA_TYPE_KEY));
        this.mExercisePager = (NonSwipeableViewPager) findViewById(R.id.pager);
        if (this.exerciseData.getLevel().intValue() == -1) {
            this.toolbarExersiceText.setText(getString(R.string.custom_workout));
        } else if (this.mExType == ExerciseType.CHALLENGE) {
            this.toolbarExersiceText.setText(getString(R.string.day) + " " + this.exerciseData.getLevel());
        } else {
            this.toolbarExersiceText.setText(ResourceService.getInstance().getString(this.exerciseData.getNameKey(), this) + " " + this.exerciseData.getLevel());
        }
        this.toolbarExersiceText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        this.toolbarExersiceText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this));
        this.exersiceRound.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this));
        if ($assertionsDisabled || this.mExercisePager != null) {
            this.exercices = this.exerciseData.getExercises();
            this.mAdapter = new ExerciseAdapter(getSupportFragmentManager(), this.exercices, this.exerciseData.getLevel().intValue());
            this.mExercisePager.setAdapter(this.mAdapter);
            final StepperIndicator indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);
            indicator.setStepCount(this.exercices.size());
            if ($assertionsDisabled || indicator != null) {
                indicator.setViewPager(this.mExercisePager, true);
                indicator.addOnStepClickListener(new OnStepClickListener() {
                    public void onStepClicked(int step) {
                    }
                });
                final int listSize = this.exerciseData.getExercises().size();
                indicator.setStepCount(listSize);
                this.mExercisePager.addOnPageChangeListener(new OnPageChangeListener() {
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        ExerciseActivity.this.exersiceRound.setText(ExerciseActivity.this.getString(R.string.round) + " " + (indicator.getCurrentStep() + 1) + "/" + listSize);
                        ExerciseActivity.this.setRepeatition(0, ((Exercise) ExerciseActivity.this.exercices.get(indicator.getCurrentStep())).getRepetition());
                        ExerciseActivity.this.setTMSpeed(((Exercise) ExerciseActivity.this.exercices.get(indicator.getCurrentStep())).getTmSpeed());
                    }

                    public void onPageSelected(int position) {
                    }

                    public void onPageScrollStateChanged(int state) {
                    }
                });
                this.iRestartLevelListener = new IRestartLevel() {
                    public void restartButtonClicked() {
                        ExerciseActivity.this.showSureDialog(false);
                    }
                };
                return;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    public void setRepeatition(int current, int count) {
        this.exersiceRepeatition.setVisibility(count == 0 ? 4 : 0);
        this.exersiceRepeatition.setText(getString(R.string.repeatition) + " " + (current + 1) + "/" + count);
    }

    public NonSwipeableViewPager getViewPager() {
        return this.mExercisePager;
    }

    public void setTMSpeed(Integer speed) {
        if (this.exersiceRepeatition.getVisibility() == 0) {
            ((LayoutParams) this.layoutTread.getLayoutParams()).addRule(3, R.id.exersiceRepeatition);
            ((LayoutParams) this.mExercisePager.getLayoutParams()).addRule(3, R.id.treadmill_layout);
        } else {
            ((LayoutParams) this.layoutTread.getLayoutParams()).addRule(3, R.id.stepper_indicator);
            ((LayoutParams) this.mExercisePager.getLayoutParams()).addRule(3, R.id.exersiceRepeatition);
        }
        LinearLayout linearLayout = this.layoutTread;
        int i = (speed == null || speed.intValue() == -1) ? 8 : 0;
        linearLayout.setVisibility(i);
        if (speed != null) {
            this.tmSpeed.setText(getString(R.string.treadmill_speed, new Object[]{Integer.valueOf(speed.intValue())}));
        }
    }

    public MoveReason getMoveReason() {
        return this.mMoveReason;
    }

    public void onBackPressed() {
        showSureDialog(true);
    }

    private void showSureDialog(final boolean exit) {
        new Builder(this).title((int) R.string.back_dialog_title).content((int) R.string.back_dialog_content).positiveText((int) R.string.back_dialog_positive).negativeText((int) R.string.back_dialog_negative).onPositive(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (exit) {
                    ExerciseActivity.this.finish();
                } else {
                    Intent intent = ExerciseActivity.this.getIntent();
                    ExerciseActivity.this.finish();
                    ExerciseActivity.this.startActivity(intent);
                }
                dialog.dismiss();
            }
        }).onNegative(new SingleButtonCallback() {
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToFinish() {
        SoundHelper.getInstance().playSound(this, "finish");
        Intent intent = new Intent(this, FinishActivity.class);
        intent.putExtra(ConsKeys.EXERCISE_DATA_KEY, this.exerciseData);
        intent.putExtra(ConsKeys.EXERCISE_DATA_TYPE_KEY, this.mExType.getType());
        startActivity(intent);
        this.isFinish = true;
        finish();
    }

    protected void onStop() {
        if (!this.isFinish) {
            Fragment fr = getVisibleFragment();
            if (fr != null) {
                ((ExerciseFragment) fr).onPauseState();
            }
        }
        super.onStop();
    }

    @SuppressLint({"RestrictedApi"})
    public Fragment getVisibleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible() && ((ExerciseFragment) fragment).getPage() == this.mExercisePager.getCurrentItem()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public void onNavigation(MoveReason moveReason) {
        this.mMoveReason = moveReason;
        switch (moveReason) {
            case MANUAL_RIGHT:
            case NATURAL_RIGHT:
                this.mExercisePager.setCurrentItem(this.mExercisePager.getCurrentItem() + 1);
                return;
            default:
                this.mExercisePager.setCurrentItem(this.mExercisePager.getCurrentItem() - 1);
                return;
        }
    }
}
