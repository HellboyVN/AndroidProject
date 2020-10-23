package hb.me.homeworkout.gym.buttlegs.buttlegspro.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.R;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseActivity.MoveReason;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.ExerciseFragment;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.exersices.listeners.IRestartLevel;
import hb.me.homeworkout.gym.buttlegs.buttlegspro.listeners.IExerciseNavigation;

public class DialogService {
    static final /* synthetic */ boolean $assertionsDisabled = (!DialogService.class.desiredAssertionStatus());
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

    public void showPauseDialog(final Activity activity, final ExerciseFragment exerciseFragment, final IRestartLevel iRestartLevel, final IExerciseNavigation iExerciseNavigation, boolean isClosePrev) {
        int i = 0;
        final Dialog dialog = new Dialog(activity, 16973840);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pause_dialog);
        ToggleButton soundToggle = (ToggleButton) dialog.findViewById(R.id.soundToggle);
        FloatingActionButton pauseFabPlay = (FloatingActionButton) dialog.findViewById(R.id.pauseFabPlay);
        FloatingActionButton pauseFabRestart = (FloatingActionButton) dialog.findViewById(R.id.pauseFabRestart);
        ((TextView) dialog.findViewById(R.id.pauseTitle)).setTypeface(TypeFaceService.getInstance().getDensRegular(activity));
        AppCompatImageView pauseFabNext = (AppCompatImageView) dialog.findViewById(R.id.pauseFabNext);
        AppCompatImageView pauseFabPrevious = (AppCompatImageView) dialog.findViewById(R.id.pauseFabPrevious);
        if (isClosePrev) {
            i = 4;
        }
        pauseFabPrevious.setVisibility(i);
        soundToggle.setChecked(SharedPrefsService.getInstance().getSoundStatus(activity));
        soundToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPrefsService.getInstance().setSoundStatus(activity, b);
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
                exerciseFragment.onPlayState();
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

    public void showSharePhotoDialog(final Context context) {
        final Dialog sharePhotoDialog = new Dialog(context);
        sharePhotoDialog.requestWindowFeature(1);
        sharePhotoDialog.setContentView(R.layout.share_result_dialog);
        sharePhotoDialog.setCancelable(true);
        sharePhotoDialog.show();
        Window window = sharePhotoDialog.getWindow();
        if ($assertionsDisabled || window != null) {
            window.setLayout(-1, -2);
            AppCompatButton dRCancel = (AppCompatButton) sharePhotoDialog.findViewById(R.id.dRCancel);
            AppCompatButton dRSharePhoto = (AppCompatButton) sharePhotoDialog.findViewById(R.id.dRSharePhoto);
            AppCompatTextView dResultDesc = (AppCompatTextView) sharePhotoDialog.findViewById(R.id.dResultDesc);
            ((AppCompatTextView) sharePhotoDialog.findViewById(R.id.dResultTitle)).setTypeface(TypeFaceService.getInstance().getRobotoRegular(context));
            dResultDesc.setTypeface(TypeFaceService.getInstance().getRobotoRegular(context));
            dRSharePhoto.setTypeface(TypeFaceService.getInstance().getRobotoRegular(context));
            dRCancel.setTypeface(TypeFaceService.getInstance().getRobotoRegular(context));
            dRCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (sharePhotoDialog != null) {
                        sharePhotoDialog.dismiss();
                    }
                }
            });
            dRSharePhoto.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    EmailHelper.getInstance().sendEmail(context, ConsKeys.EMAIL_FITFIT_RESULT, "Instagram shared result photo", "Please send 2 photos BEFORE and AFTER");
                    if (sharePhotoDialog != null) {
                        sharePhotoDialog.dismiss();
                    }
                }
            });
            return;
        }
        throw new AssertionError();
    }

    public void showRestartProgressConfirmDialog(Context context, SingleButtonCallback listener) {
        new Builder(context).title((int) R.string.reset_progress).positiveText((int) R.string.reset_positive_label).negativeText((int) R.string.setting_cancel).content((int) R.string.reset_progress_note).onPositive(listener).show();
    }
}
