package hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.views;

import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.R;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices.ExerciseActivity.MoveReason;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices.FragmentExercise;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices.IExerciseNavigation;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.exersices.IRestartLevel;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.SharedPrefsService;
import hb.fitnesschallenge.homeworkout.homeworkouts.noequipment.fitnesspro.utils.TypeFaceService;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;

public class DialogService {
    private static DialogService INSTANCE = null;
    private long COUNT_DOWN_TIME = 2000;
    private long GO_TIME = 1500;
    private long READY_TIME = 3000;
    private final Context context;
    boolean isLongRest;
    CountDownTimer mCountDownTimer;

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DialogService(context);
        }
    }

    public static DialogService getInstance() {
        if (INSTANCE != null) {
            return INSTANCE;
        }
        throw new IllegalStateException("service not initialized");
    }

    private DialogService(Context context) {
        this.context = context;
    }

    public void showPauseDialog(final Activity activity, final FragmentExercise fragmentExercise, final IRestartLevel iRestartLevel, final IExerciseNavigation iExerciseNavigation, boolean isClosePrev) {
        int i = 0;
        final Dialog dialog = new Dialog(activity, 16973840);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pause_dialog);
        ToggleButton soundToggle = (ToggleButton) dialog.findViewById(R.id.soundToggle);
        FloatingActionButton pauseFabPlay = (FloatingActionButton) dialog.findViewById(R.id.pauseFabPlay);
        FloatingActionButton pauseFabRestart = (FloatingActionButton) dialog.findViewById(R.id.pauseFabRestart);
        AppCompatImageView pauseFabNext = (AppCompatImageView) dialog.findViewById(R.id.pauseFabNext);
        AppCompatImageView pauseFabPrevious = (AppCompatImageView) dialog.findViewById(R.id.pauseFabPrevious);
        ((TextView) dialog.findViewById(R.id.pauseTitle)).setTypeface(TypeFaceService.getInstance().getDensRegular(activity));
        if (isClosePrev) {
            i = 4;
        }
        pauseFabPrevious.setVisibility(i);
        soundToggle.setChecked(SharedPrefsService.getInstance().getSoundStatus(activity));
        soundToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SharedPrefsService.getInstance().setSoundStatus(activity, true);
                } else {
                    SharedPrefsService.getInstance().setSoundStatus(activity, false);
                }
            }
        });
        pauseFabRestart.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                iRestartLevel.restartButtonClicked();
            }
        });
        pauseFabPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                fragmentExercise.onPlayState();
            }
        });
        pauseFabNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                iExerciseNavigation.onNavigation(MoveReason.MANUAL_RIGHT);
            }
        });
        pauseFabPrevious.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                iExerciseNavigation.onNavigation(MoveReason.MANUAL_LEFT);
            }
        });
        dialog.show();
    }

    public void showRestartProgressConfirmDialog(Context context, SingleButtonCallback listener) {
        new Builder(context).title((int) R.string.reset_progress).positiveText((int) R.string.reset_positive_label).negativeText((int) R.string.setting_cancel).content((int) R.string.reset_progress_note).onPositive(listener).show();
    }
}
