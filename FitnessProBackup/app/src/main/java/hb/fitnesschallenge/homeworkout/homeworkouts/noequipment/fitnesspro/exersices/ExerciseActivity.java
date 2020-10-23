package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices.StepperIndicator.OnStepClickListener;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.finish.FinishActivity;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.home.tabs.tabfour.type.ExerciseType;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.Exercise;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.models.LevelData;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ConsKeys;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.ResourceService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SoundHelper;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity implements IExerciseNavigation {
    static final /* synthetic */ boolean $assertionsDisabled = (!ExerciseActivity.class.desiredAssertionStatus());
    static NonSwipeableViewPager mExercisePager;
    List<Exercise> exercices;
    LevelData exerciseData;
    @BindView(R.id.exersiceRound)
    TextView exersiceRound;
    IRestartLevel iRestartLevelListener;
    boolean isFinish = false;
    ExerciseAdapter mAdapter;
    private ExerciseType mExType;
    private MoveReason mMoveReason = MoveReason.NATURAL_RIGHT;
    @BindView(R.id.toolbarExersice)
    Toolbar toolbarExersice;
    @BindView(R.id.toolbarExersiceText)
    TextView toolbarExersiceText;
    @BindView(R.id.btn_pause)
    ToggleButton soundToggle;

    public enum MoveReason {
        NATURAL_RIGHT,
        MANUAL_LEFT,
        MANUAL_RIGHT
    }

    public IRestartLevel getiRestartLevelListener() {
        return this.iRestartLevelListener;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_excercise);
        setVolumeControlStream(3);
        ButterKnife.bind((Activity) this);
        setSupportActionBar(this.toolbarExersice);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //hellboy add
        soundToggle.setChecked(SharedPrefsService.getInstance().getSoundStatus(getApplicationContext()));
        soundToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SharedPrefsService.getInstance().setSoundStatus(getApplicationContext(), true);
                } else {
                    SharedPrefsService.getInstance().setSoundStatus(getApplicationContext(), false);
                }
            }
        });

        this.exerciseData = (LevelData) getIntent().getExtras().getSerializable(ConsKeys.EXERCISE_DATA_KEY);
        this.mExType = ExerciseType.getByType(getIntent().getExtras().getInt(ConsKeys.EXERCISE_DATA_TYPE_KEY));
        mExercisePager = (NonSwipeableViewPager) findViewById(R.id.pager);
        if (this.exerciseData.getLevel().intValue() == -1) {
            this.toolbarExersiceText.setText(getString(R.string.custom_workout));
        } else if (this.mExType == ExerciseType.CHALLENGE) {
            this.toolbarExersiceText.setText(getString(R.string.day) + " " + this.exerciseData.getLevel());
        } else {
            this.toolbarExersiceText.setText(ResourceService.getInstance().getString(this.exerciseData.getNameKey(), this) + " " + this.exerciseData.getLevel());
        }
        int titleColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);
        switch (SharedPrefsService.getInstance().getGender(this)) {
            case 1:
                titleColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDarkMale);
                break;
            case 2:
                titleColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDarkFemale);
                break;
        }
        this.toolbarExersiceText.setTextColor(titleColor);
        this.toolbarExersiceText.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this));
        this.exersiceRound.setTypeface(TypeFaceService.getInstance().getRobotoRegular(this));
        if ($assertionsDisabled || mExercisePager != null) {
            this.exercices = this.exerciseData.getExercises();
            this.mAdapter = new ExerciseAdapter(getSupportFragmentManager(), this.exercices, this.exerciseData.getLevel().intValue());
            mExercisePager.setAdapter(this.mAdapter);
            mExercisePager.setOffscreenPageLimit(0);
            final StepperIndicator indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);
            indicator.setStepCount(this.exercices.size());
            if ($assertionsDisabled || indicator != null) {
                indicator.setViewPager(mExercisePager, true);
                if (this.exercices.size() > 10) {
                    indicator.setVisibility(4);
                }
                indicator.addOnStepClickListener(new OnStepClickListener() {
                    public void onStepClicked(int step) {
                    }
                });
                final int listSize = this.exerciseData.getExercises().size();
                indicator.setStepCount(listSize);
                mExercisePager.addOnPageChangeListener(new OnPageChangeListener() {
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        ExerciseActivity.this.exersiceRound.setText(ExerciseActivity.this.getString(R.string.round) + " " + (indicator.getCurrentStep() + 1) + "/" + listSize);
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
        SoundHelper.getInstance().playSound("finish");
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
                ((FragmentExercise) fr).onPauseState();
            }
        }
        super.onStop();
    }

    public Fragment getVisibleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible() && ((FragmentExercise) fragment).getPage() == mExercisePager.getCurrentItem()) {
                    return fragment;
                }
            }
        }
        return null;
    }

    public NonSwipeableViewPager getViewPager() {
        return mExercisePager;
    }

    public MoveReason getMoveReason() {
        return this.mMoveReason;
    }

    public void onNavigation(MoveReason moveReason) {
        this.mMoveReason = moveReason;
        switch (moveReason) {
            case MANUAL_RIGHT:
            case NATURAL_RIGHT:
                mExercisePager.setCurrentItem(mExercisePager.getCurrentItem() + 1);
                return;
            default:
                mExercisePager.setCurrentItem(mExercisePager.getCurrentItem() - 1);
                return;
        }
    }
}
